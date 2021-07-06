package services

import org.scalatestplus.play._

class WatchlistServiceSpec extends PlaySpec {
  "Watchlist" should {
    "add entries to list" in {
      val service = new WatchlistService
      service.addWatchStock("goog")

      service.watchStocks must contain ("goog")
    }

    "not add duplicate entries to list" in {
      val service = new WatchlistService
      service.addWatchStock("goog")
      service.addWatchStock("goog")

      service.watchStocks.length must be (1)
      service.watchStocks must contain ("goog")
    }

    "get watchlist" in {
      val service = new WatchlistService
      service.watchStocks += "axp"

      service.getWatchStocks must contain ("axp")
    }

    "delete item from watchlist" in {
      val service = new WatchlistService
      service.watchStocks += "goog"

      service.removeWatchStock("goog")
      service.watchStocks.toList must not contain ("goog")
    }
  }
}
