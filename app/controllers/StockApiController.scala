package controllers

import models.{StockQuoteDTO, StockRequestDTO}
import play.api.libs.json.{JsError, JsPath, JsResult, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, BaseController, ControllerComponents}
import services.FinanceService

import scala.util.{Failure, Success, Try}
import javax.inject.Inject

@Singleton
class StockApiController @Inject()(val controllerComponents: ControllerComponents, financeService: FinanceService) extends BaseController {
  def getSingleStockPrice = Action(parse.json) { request =>
    val requestToParse: JsResult[StockRequestDTO] = Json.fromJson[StockRequestDTO](request.body)
    val parseResult = requestToParse match {
      case JsSuccess(value: StockRequestDTO, _) => Left(value)
      case e @ JsError(errVal) => Right(errVal.toString)
    }
    parseResult match {
      case Right(err) => BadRequest(err)
      case Left(request) => {
        val stockTry = Try(financeService.getStockFromSymbol(request.symbol))
        stockTry match {
          case Success(stock) => {
            if (request.startDate.isEmpty) {
              Ok(Json.toJson(StockQuoteDTO(stock.getQuote.getPrice.floatValue())))
            } else {
              Ok("test")
            }
          }
        }
      }
    }
  }
}
