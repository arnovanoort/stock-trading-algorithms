package nl.arnovanoort.tradingalgorithms.domain

import java.util.*

data class ExecutionAlgorithm (
    val id: UUID,
    val name: ExecutionAlgorithmName
)

enum class ExecutionAlgorithmName{
    MOMENTUM
}