package services

import yahoofinance.{Stock, YahooFinance}

/**
 * This class exists just to make it easier to abstract away the YahooFinance
 * library when unit testing. I don't know of a good way to make a mock for
 * static functions the way this is used.
 */

class YahooFinanceWrapper {
  def getStockRaw(symbol:String): Stock = {
    YahooFinance.get(symbol)
  }
}
