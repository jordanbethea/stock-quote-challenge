package models

import play.api.libs.json.{Json, Reads, Writes}

object StockQuoteDTO{
  implicit val requestWrites : Writes[StockQuoteDTO] = Json.writes[StockQuoteDTO]
  implicit val requestReads : Reads[StockQuoteDTO] = Json.reads[StockQuoteDTO]
}

case class StockQuoteDTO(currentPrice: BigDecimal)
