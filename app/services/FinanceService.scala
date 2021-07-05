package services
import yahoofinance.{Stock, YahooFinance}

import javax.inject.Inject
import scala.util.{Failure, Success, Try}


class FinanceService @Inject()(val yahooF:YahooFinanceWrapper) {

  def getStockFromSymbol(symbol:String): Either[Stock, String] = {
    val stockTry = Try(Option(yahooF.getStockRaw(symbol)))
    stockTry match {
      case Failure(ex) => Right(ex.toString)
      case Success(value) => {
        value match {
          case None => Right(s"No stock with code ${symbol} was found")
          case Some(stock) => Left(stock)
        }
      }
    }
  }

  /**
   * Checks for current price of given stock symbol
   * @param symbol  4 letter stock symbol used on exchange
   * @return  Left side is Price, or None if stock or quote could not be found. Right side is exception error messages
   */
  def getCurrentPriceForStock(symbol: String): Either[Option[BigDecimal], String] = {
    //this function is more complicated than you'd think because I wanted to contain the whole thing in a single
    //step for the service, and it requires two nested function calls that could return nulls or exceptions, and I
    //wanted to handle all of them here.
    val stockTry = Try(Option(yahooF.getStockRaw(symbol))) //could return IOException
    stockTry match {
      case Success(stock) => {
        stock match {
          case None => Left(None)
          case Some(s) => {

            val priceTry = Try(Option(s.getQuote.getPrice)) //getQuote could return IOException
            priceTry match {
              case Success(price) => Left(price.flatMap(p => Option(scala.BigDecimal(p))))
              case Failure(ex) => Right(ex.toString)
            }
          }
        }
      }
      case Failure(ex) => Right(ex.toString)
    }
  }
}
