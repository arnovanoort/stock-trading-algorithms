package nl.arnovanoort.tradingalgorithms.implementation

import nl.arnovanoort.tradingalgorithms.domain.StockPrice
import nl.arnovanoort.tradingalgorithms.repository.ExecutionResultRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import java.time.LocalDate
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MomentumAlgorithmTest {

    private val momentumAlgorithm = MomentumAlgorithm()

    @Mock
    private lateinit var executionResultRepository: ExecutionResultRepository


//    @Test
//    fun testCallwithoutLookback(){
//    momentumAlgorithm.stockMarketRepository = mockk<StockMarketRepository>(relaxed = true)
//        assertThrows<MomentumException>{
//            momentumAlgorithm.execute(UUID.randomUUID(), HashMap())
//        }
//    }
    @Test
    fun testDifferenceMore() {
        assertEquals(listOf(1.5f, 0.8888889f), momentumAlgorithm.calculateDifference(listOf(50.00f, 100.00f, 90.00f), listOf()))
    }

    @Test
    fun testDifferenceLess() {
        assertEquals(listOf(0.8888889f), momentumAlgorithm.calculateDifference(listOf(100.00f, 90.00f), listOf()))
    }

    @Test
    fun testDifferenceMultiple() {
        assertEquals(listOf(1.5f, 0.8888889f), momentumAlgorithm.calculateDifference(listOf(50.00f, 100.00f, 90.00f), listOf()))
    }

    @Test
    fun testMomentum() {
        val appleStockUUid = UUID.randomUUID();
        val appleStockPrices1:List<StockPrice> = listOf(
            StockPrice(UUID.randomUUID(), 19.85f, 17.88f,20.01f,17.63f, 8765954400, appleStockUUid, LocalDate.of(2014, 1, 1)),
            StockPrice(UUID.randomUUID(), 17.95f, 18.79f,19.69f,17.83f, 5880366800, appleStockUUid, LocalDate.of(2014, 1, 8)),
            StockPrice(UUID.randomUUID(), 18.69f, 19.17f,19.61f,18.67f, 5001698800, appleStockUUid, LocalDate.of(2014, 1, 15)),
            StockPrice(UUID.randomUUID(), 19.21f, 21.07f,21.41f,18.26f, 6435060800, appleStockUUid, LocalDate.of(2014, 1, 22)),
            StockPrice(UUID.randomUUID(), 21.14f, 22.61f,23.01f,20.73f, 5735668400, appleStockUUid, LocalDate.of(2014, 1, 29)),
            StockPrice(UUID.randomUUID(), 22.64f, 23.23f,23.76f,22.23f, 4827739200, appleStockUUid, LocalDate.of(2014, 2, 5)),
            StockPrice(UUID.randomUUID(), 23.38f, 23.90f,24.86f,23.14f, 4140344000, appleStockUUid, LocalDate.of(2014, 2, 12)),
            StockPrice(UUID.randomUUID(), 23.73f, 25.63f,25.73f,23.32f, 3748308000, appleStockUUid, LocalDate.of(2014, 2, 19)),
            StockPrice(UUID.randomUUID(), 25.76f, 25.19f,25.93f,24.03f, 6105680000, appleStockUUid, LocalDate.of(2014, 2, 26)),
            StockPrice(UUID.randomUUID(), 25.15f, 27.00f,27.01f,23.80f, 5441120800, appleStockUUid, LocalDate.of(2014, 3, 5)),
            StockPrice(UUID.randomUUID(), 27.06f, 29.73f,29.94f,26.93f, 3281632800, appleStockUUid, LocalDate.of(2014, 3, 12)),
            StockPrice(UUID.randomUUID(), 29.70f, 27.59f,29.81f,26.57f, 4294378400, appleStockUUid, LocalDate.of(2014, 3, 19))
        )
        val appleStockPrices:List<StockPrice> = appleStockPrices1.flatMap { stockPrices -> listOf(stockPrices,stockPrices,stockPrices,stockPrices,stockPrices,stockPrices,stockPrices)}
        val momentum = momentumAlgorithm.calculateStockMomentum(appleStockPrices)
        assertEquals(42.709328f, momentum)
    }
}