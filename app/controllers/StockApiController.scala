package controllers

import play.api.libs.json.JsValue
import play.api.mvc.{Action, BaseController, ControllerComponents}
import yahoofinance.YahooFinance

import javax.inject.Inject

@Singleton
class StockApiController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

}
