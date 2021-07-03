package models
import play.api.libs.json.{Json, Reads, Writes}

import java.time.LocalDateTime

object StockRequestDTO {
  implicit val requestWrites : Writes[StockRequestDTO] = Json.writes[StockRequestDTO]
  implicit val requestReads : Reads[StockRequestDTO] = Json.reads[StockRequestDTO]
}

case class StockRequestDTO(symbol:String, startDate: Option[LocalDateTime]) {

}
