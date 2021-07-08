package controllers

import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.mvc.Request
import play.api.test._
import play.api.test.Helpers._
import services.{FinanceService, WatchlistService}
import yahoofinance.Stock

import scala.collection.mutable.ListBuffer

class StockApiControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockFactory {
  "stock watchlist" should {
    "return watchlist on GET" in {
      val financeServiceMock = mock[FinanceService]
      val watchlistServiceMock = mock[WatchlistService]
      (watchlistServiceMock.getWatchStocks _).expects().returns(List("goog", "axp"))
      val controller = new StockApiController(stubControllerComponents(), financeServiceMock, watchlistServiceMock)
      val watchResult = controller.getWatchStocks().apply(FakeRequest(GET, "/"))

      status(watchResult) mustBe OK
      contentAsString(watchResult) mustBe (Json.toJson(List("goog", "axp")).toString())
    }
    "add to watchlist on PUT" in {
      val financeServiceMock = mock[FinanceService]
      (financeServiceMock.getStockFromSymbol _).expects("goog").returns(Left(new Stock("goog")))
      val watchlistServiceMock = mock[WatchlistService]
      (watchlistServiceMock.addWatchStock _).expects("goog").returns(Option("goog"))
      val controller = new StockApiController(stubControllerComponents(), financeServiceMock, watchlistServiceMock)
      val request = FakeRequest(PUT, "/api/watchlist").withJsonBody(Json.obj("code" -> "goog"))
      val watchResult = controller.addWatchStock().apply(request)

      status(watchResult) mustBe OK
      contentAsString(watchResult) mustBe ("Stock goog added to watchlist")
    }

    "remove from watchlist on DELETE" in {
      val financeServiceMock = mock[FinanceService]
      val watchlistServiceMock = mock[WatchlistService]
      (watchlistServiceMock.removeWatchStock _).expects("goog")

      val controller = new StockApiController(stubControllerComponents(), financeServiceMock, watchlistServiceMock)
      val request = FakeRequest(DELETE, "/api/watchlist").withJsonBody(Json.obj("code" -> "goog"))
      val removeResult = controller.removeWatchStock().apply(request)

      status(removeResult) mustBe OK
      contentAsString(removeResult) mustBe ("Stock goog removed from watchlist")
    }
  }

  "stock price retrieval" should {
    "retrieve single stock price" in {
      val financeServiceMock = mock[FinanceService]
      (financeServiceMock.getCurrentPriceForStock _).expects("goog").returns(Left(Some(BigDecimal(123.45))))
      val watchlistServiceMock = mock[WatchlistService]

      val controller = new StockApiController(stubControllerComponents(), financeServiceMock, watchlistServiceMock)
      val priceResult = controller.getSingleStockPrice("goog", None).apply(FakeRequest())

      status(priceResult) mustBe OK
      contentAsString(priceResult) mustBe ("{\"currentPrice\":123.45}")
    }
  }
}
