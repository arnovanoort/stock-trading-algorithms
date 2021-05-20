package nl.arnovanoort.tradingalgorithms.domain

import java.math.BigDecimal
import java.util.*

data class ExecutionResult (
    val id          : UUID,
    val name        : ExecutionResultName,
    val value       : BigDecimal,
    val executionId : UUID,
    val stockId     : UUID
)

data class CreateExecutionResult (
    val name        : ExecutionResultName,
    val value       : BigDecimal,
    val stockId     : UUID,
    val executionId : UUID
)

enum class ExecutionResultName{
    MOMENTUM
}


