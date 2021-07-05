package controllers

import models.{StockQuoteDTO, StockRequestDTO}
import play.api.libs.json.{JsError, JsPath, JsResult, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, BaseController, ControllerComponents}
import services.FinanceService

import javax.inject.{Inject, Singleton}
import collection.mutable.ListBuffer

@Singleton
class StockApiController @Inject()(val controllerComponents: ControllerComponents, financeService: FinanceService) extends BaseController {

  val watchStocks = ListBuffer[String]()

  def getWatchStocks = Action { request =>
    Ok(Json.toJson(watchStocks.toList))
  }

  def addWatchStock = Action{ request =>
    val requestToParse = request.body.asJson
    val stockCode:Option[String] = requestToParse.flatMap(r => Option((r \ "code").as[String]))

    stockCode match {
      case None => BadRequest("Unparseable request")
      case Some(code) => {
        if(code == ""){
          BadRequest("No stock code supplied")
        } else {
          val lookupResult = financeService.getStockFromSymbol(code)
          lookupResult match {
            case Right(err) => InternalServerError(err)
            case Left(stock) => {
              if (watchStocks.contains(code)) {
                Ok("Stock already in watchlist")
              } else {
                watchStocks += code
                Ok(s"Stock ${code} added to watchlist")
              }
            }
          }
        }
      }
    }
  }

  def removeWatchStock = Action { request =>
    val requestToParse = request.body.asJson
    val stockCode = Option("") //requestToParse \ "code"
    stockCode match {
      case None => BadRequest("No stock code supplied")
      case Some(code) => {
        val result = watchStocks -= code
        Ok(s"Stock ${code} removed from watchlist")
      }
    }
  }

  def getSingleStockPrice = Action(parse.json) { request =>
    val requestToParse: JsResult[StockRequestDTO] = Json.fromJson[StockRequestDTO](request.body)
    val parseResult = requestToParse match {
      case JsSuccess(value: StockRequestDTO, _) => Left(value)
      case e @ JsError(errVal) => Right(errVal.toString)
    }
    parseResult match {
      case Right(err) => BadRequest(err)
      case Left(request) => {
        val priceResult = financeService.getCurrentPriceForStock(request.symbol)
        priceResult match {
          case Right(err) => InternalServerError(err)
          case Left(priceOpt) => {
            priceOpt match {
              case None => InternalServerError("Stock or Price not found")
              case Some(price) => Ok(Json.toJson(StockQuoteDTO(price)))
            }
          }
        }
      }
    }
  }
}
