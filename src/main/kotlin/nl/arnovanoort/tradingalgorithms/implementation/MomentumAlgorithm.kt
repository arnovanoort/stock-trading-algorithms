package nl.arnovanoort.tradingalgorithms.implementation

import nl.arnovanoort.tradingalgorithms.MomentumException
import nl.arnovanoort.tradingalgorithms.definition.TradingAlgorithm
import nl.arnovanoort.tradingalgorithms.domain.*
import nl.arnovanoort.tradingalgorithms.repository.ExecutionResultRepository
import nl.arnovanoort.tradingalgorithms.repository.StockMarketRepository
import nl.arnovanoort.tradingalgorithms.repository.StockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import kotlin.collections.HashMap


@Service
class MomentumAlgorithm : TradingAlgorithm {

    @Autowired
    private lateinit var stockMarketRepository: StockMarketRepository;

    @Autowired
    private lateinit var stockRepository: StockRepository;

    @Autowired
    private lateinit var executionResultRepository: ExecutionResultRepository


    override fun getName():ExecutionAlgorithmName {
        return ExecutionAlgorithmName.MOMENTUM
    }

    override fun execute(stockMarketUuid: UUID, params: HashMap<String, String>) {

        // create execution
        val executionAlgorithm : ExecutionAlgorithm = executionResultRepository
            .getExecutiontypeByName(
                getName().name
            )?:throw(MomentumException("Can not find executiontype"))

        // store it
        val createExecution = CreateExecution.fromParams(executionAlgorithm.id, params)
        val executionId = executionResultRepository.createExecution(createExecution)

        // calculate momentum for all stocks of a stockmarket
        stockMarketRepository
            .getStocks(stockMarketUuid)
            .forEach{ stock ->
                val stockPrices = stockRepository.getStockPrice(stock.id, createExecution.startDate, createExecution.endDate)
                val momentum = calculateStockMomentum(stockPrices)
                val executionResult = CreateExecutionResult(
                    ExecutionResultName.MOMENTUM,
                    momentum.toBigDecimal(),
                    executionId,
                    stock.id
                )
                executionResultRepository.createExecutionResult(executionResult, stock.asExecutionStock())
            }
    }

    fun calculateStockMomentum(stockPrices: List<StockPrice>): Float {
        val filtered: List<Float> = stockPrices
            .sortedByDescending { it.date }
            .take(7 * (stockPrices.size / 7)) // take a meervoud van 7
            .windowed(1, 7) // calculate on weekly stock price
            .flatten()
            .sortedBy { it.date }
            .map { it.open }
        val difference = calculateDifference(filtered, listOf())
        return calculateMomentum(difference)
    }

    fun calculateMomentum(differences: List<Float>): Float {
        return 100 * (differences.reduce{ acc, value -> acc * (value) } - 1)
    }

    fun calculateDifference(prices: List<Float>, previousPeriods: List<Float>): List<Float> {
        if (prices.size >= 2) {
            val month1 = prices.get(0)
            val month2 = prices.get(1)

            val stockReturn = ((month2 - month1) / month2) + 1
            return calculateDifference(prices.drop(1), previousPeriods + stockReturn, )
        } else {
            return previousPeriods
        }
    }
}