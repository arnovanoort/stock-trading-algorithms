package nl.arnovanoort.tradingalgorithms.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.*

data class Stock(
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("ticker")
    val ticker: String,
    @JsonProperty("assetType")
    var assetType: String,
    @JsonProperty("currency")
    var currency: String,
    @JsonProperty("dateListedNullable")
    var dateListedNullable: LocalDate?,
    @JsonProperty("dateUnListedNullable")
    var dateUnListedNullable: LocalDate?,
    @JsonProperty("stockMarketId")
    val stockMarketId: UUID

){
    fun asExecutionStock(): ExecutionStock {
        return ExecutionStock(
            id,
            name,
            ticker,
            stockMarketId
        )

    }
}
