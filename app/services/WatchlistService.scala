package services

import scala.collection.mutable.ListBuffer

class WatchlistService {
  val watchStocks = ListBuffer[String]()

  def getWatchStocks() = watchStocks.toList

  def addWatchStock(code: String) : Option[String] = {
    if(!watchStocks.contains(code)) {
      watchStocks += code
      Option(code)
    }
    else None
  }

  def removeWatchStock(code:String) = watchStocks -= code
}
