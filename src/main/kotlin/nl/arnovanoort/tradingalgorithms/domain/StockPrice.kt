package nl.arnovanoort.tradingalgorithms.domain

import java.time.LocalDate
import java.util.*

data class StockPrice(
    val id: UUID,
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float,
    val volume: Long,
    val stockId: UUID,
    val date: LocalDate
)

