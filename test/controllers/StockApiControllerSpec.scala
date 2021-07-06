package controllers

import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import services.FinanceService

import scala.collection.mutable.ListBuffer

class StockApiControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockFactory {
  "stock watchlist" should {
    "return watchlist on GET" in {
      val financeServiceMock = mock[FinanceService]
      val controller = new StockApiController(stubControllerComponents(), financeServiceMock)
      //controller.watchStocks = ListBuffer("goog", "axp")
    }
    "add to watchlist on PUT" in {

    }

    "remove from watchlist on DELETE" in {

    }
  }
}
