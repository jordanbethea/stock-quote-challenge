package services
import yahoofinance.{Stock, YahooFinance}
import scala.util.{Failure, Success, Try}


class FinanceService {

  def getStockFromSymbol(symbol:String): Stock = {
    YahooFinance.get(symbol)
  }

  /**
   * Checks for current price of given stock symbol
   * @param symbol  4 letter stock symbol used on exchange
   * @return  Left side is Price, or None if stock or quote could not be found. Right side is exception error messages
   */
  def getCurrentPriceForStock(symbol: String): Either[Option[BigDecimal], String] = {
    val stockTry = Try(Option(YahooFinance.get(symbol))) //could return IOException
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
