package nl.arnovanoort.tradingalgorithms.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class StockMarket (
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("name")
    val name: String,
){
    fun asExecutionStockMarket(): ExecutionStockMarket{
        return ExecutionStockMarket(
            id,
            name
        )
    }
}