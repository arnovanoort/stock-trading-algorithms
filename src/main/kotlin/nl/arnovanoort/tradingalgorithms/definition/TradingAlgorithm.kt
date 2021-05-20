package nl.arnovanoort.tradingalgorithms.definition

import nl.arnovanoort.tradingalgorithms.domain.ExecutionAlgorithmName
import java.util.*
import kotlin.collections.HashMap

interface TradingAlgorithm {
    fun execute(stockMarketId: UUID, params: HashMap<String, String>)

    fun getName(): ExecutionAlgorithmName
}