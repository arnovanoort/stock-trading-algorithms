package nl.arnovanoort.tradingalgorithms.repository

import nl.arnovanoort.tradingalgorithms.domain.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:application.properties")
@SpringBootTest
class ExecutionResultRepositoryTest {

    @Autowired
    private lateinit var executionResultRepository: ExecutionResultRepository

    @MockBean
    private lateinit var stockMarketRepository: StockMarketRepository

    @Test
    fun testGetExecutionType() {
        val result = executionResultRepository.getExecutiontypeByName("MOMENTUM")
        assertEquals("MOMENTUM", result?.name!!.name)
    }

    @Test
    fun testCreateExecution() {

        // execute
        val execution = executionResultRepository.createExecution(createExecution)

        // verify
        val ex = executionResultRepository.getExecution(execution)
        assert(ex?.id != null)
        assertEquals(ex?.startDate, LocalDate.of(2021, 5, 3))
        assertEquals(ex?.endDate, LocalDate.of(2021, 5, 10))
        assertEquals(ex?.executionTypeUuid.toString(), "0ce2585e-8d35-4700-bd85-1ec08ed8815b")
    }

    @Test
    fun testCreateExecutionResult() {
        val stockMarketId = UUID.randomUUID()
        Mockito.`when`(stockMarketRepository.get(stockMarketId)).thenReturn(StockMarket(stockMarketId, "NASDAQ"))
        val executionStock: ExecutionStock = ExecutionStock(UUID.randomUUID(), "Amazon", "AMZN", stockMarketId)
        val executionId = executionResultRepository.createExecution(createExecution)
        val momentum = 11.11f.toBigDecimal()

        val createExecutionResult: CreateExecutionResult = CreateExecutionResult(
            ExecutionResultName.MOMENTUM,
            momentum,
            executionStock.id,
            executionId
            )
        val executionResultId = executionResultRepository.createExecutionResult(createExecutionResult, executionStock)
        val executionResult = executionResultRepository.getExecutionResult(executionResultId)
        assertTrue(executionResultId != null)
        assertEquals(momentum, executionResult?.value  )
    }

    companion object {
        // prepare
        val executionType = ExecutionAlgorithm(
            UUID.fromString("0ce2585e-8d35-4700-bd85-1ec08ed8815b"),
            ExecutionAlgorithmName.MOMENTUM
        )
        val params = mapOf(
            "endDate" to "10-05-2021",
            "lookback" to "7"
        )
        val createExecution: CreateExecution = CreateExecution.fromParams(executionType.id, params)
    }
}
