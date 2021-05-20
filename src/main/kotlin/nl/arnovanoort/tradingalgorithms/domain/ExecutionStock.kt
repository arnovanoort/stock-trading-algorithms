package nl.arnovanoort.tradingalgorithms.domain

import java.util.*

data class ExecutionStock (
    val id              : UUID,
    val name            : String,
    val ticker          : String,
    val stockMarketId   : UUID
)