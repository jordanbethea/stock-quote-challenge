package services
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import org.scalamock.scalatest.MockFactory
import yahoofinance.Stock


class FinanceServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockFactory {

  "getStockFromSymbol" should {

    "Return error message if stock not found (null from wrapper)" in {
      val mockWrapper = mock[YahooFinanceWrapper]
      (mockWrapper.getStockRaw _).expects("FAKESTOCK").returns(null)
      val testService = new FinanceService(mockWrapper)
      val testStock = testService.getStockFromSymbol("FAKESTOCK")
      testStock mustBe Right("No stock with code FAKESTOCK was found")
    }

    "Return stock object if stock is found" in {
      val mockWrapper = mock[YahooFinanceWrapper]
      val mockStock = mock[Stock]
      (mockStock.getName _).expects().returns("Google")
      (mockWrapper.getStockRaw _).expects("goog").returns(mockStock)
      val testService = new FinanceService(mockWrapper)

      val result = testService.getStockFromSymbol("goog")
      result mustBe Left(mockStock)
      result.left.getOrElse(null).getName mustBe "Google"
    }

  }
}
