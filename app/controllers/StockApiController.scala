package controllers

import models.{StockQuoteDTO, StockRequestDTO}
import play.api.libs.json.{JsError, JsPath, JsResult, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, BaseController, ControllerComponents}
import services.{FinanceService, WatchlistService}

import javax.inject.{Inject, Singleton}
import collection.mutable.ListBuffer

@Singleton
class StockApiController @Inject()(val controllerComponents: ControllerComponents,
                                   financeService: FinanceService,
                                   watchlistService: WatchlistService)
  extends BaseController {

  //val watchStocks = ListBuffer[String]()

  def getWatchStocks = Action { request =>
    Ok(Json.toJson(watchlistService.getWatchStocks()))
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
              val result = watchlistService.addWatchStock(code)
              result match {
                case None => Ok("Stock already in watchlist")
                case Some(_) => Ok(s"Stock ${code} added to watchlist")
              }
            }
          }
        }
      }
    }
  }

  def removeWatchStock = Action { request =>
    val requestToParse = request.body.asJson
    val stockCode: Option[String] = requestToParse.flatMap(r => Option((r \ "code").as[String]))
    stockCode match {
      case None => BadRequest("No stock code supplied")
      case Some(code) => {
        val result = watchlistService.removeWatchStock(code)
        Ok(s"Stock ${code} removed from watchlist")
      }
    }
  }

  def getSingleStockPrice(code:String, avgMode:Option[String]) = Action { request =>

    val priceResult = financeService.getCurrentPriceForStock(code)
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
