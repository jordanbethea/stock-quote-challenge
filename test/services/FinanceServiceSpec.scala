package services
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import org.scalamock.scalatest.MockFactory
import yahoofinance.Stock
import yahoofinance.quotes.stock.StockQuote


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

  "getCurrentPriceForStock" should {
    "Return none if stock does not exist" in {
      val mockWrapper = mock[YahooFinanceWrapper]
      (mockWrapper.getStockRaw _).expects("FAKESTOCK").returns(null)
      val testService = new FinanceService(mockWrapper)

      val result = testService.getCurrentPriceForStock("FAKESTOCK")
      result mustBe Left(None)
    }

    "Return error if Yahoo Finance is not accessible (IOException)" in {
      val mockWrapper = mock[YahooFinanceWrapper]
      val mockThrowable = new Throwable("Exception Text")
      (mockWrapper.getStockRaw _).expects("FAKESTOCK").throws(mockThrowable)
      val testService = new FinanceService(mockWrapper)

      val result = testService.getCurrentPriceForStock("FAKESTOCK")
      result mustBe Right("java.lang.Throwable: Exception Text")
    }

    "Return stock value for valid stock" in {
      //using actual Stock and StockQuote classes here because ScalaMock has trouble mocking java classes
      val actualStock = new Stock("stock")
      actualStock.getQuote
      val mockWrapper = mock[YahooFinanceWrapper]
      val quote = new StockQuote("goog")
      val bigDecimalJ = new java.math.BigDecimal(238.17, new java.math.MathContext(5))
      quote.setPrice(bigDecimalJ)
      val stock = new Stock("goog")
      stock.setQuote(quote)
      (mockWrapper.getStockRaw _).expects("goog").returns(stock)
      val testService = new FinanceService(mockWrapper)

      val result = testService.getCurrentPriceForStock("goog")
      result mustBe Left(Some(BigDecimal(bigDecimalJ)))
    }
  }
}
