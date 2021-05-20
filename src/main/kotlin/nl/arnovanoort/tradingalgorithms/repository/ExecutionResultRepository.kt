package nl.arnovanoort.tradingalgorithms.repository

import nl.arnovanoort.tradingalgorithms.domain.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Repository
class ExecutionResultRepository {

    @Autowired
    private lateinit var stockMarketRepository: StockMarketRepository;

    fun getExecutiontypeByName(executionName: String): ExecutionAlgorithm?{
        // TODO: cache executiontype
        return transaction {
            DBExecutionType.select {DBExecutionType.name eq executionName}
                .map { mapToExecutionType(it) }
                .firstOrNull()
        }
    }

    fun getExecution(id: UUID): Execution? {
        return transaction {
            DBExecution
                .select { DBExecution.id eq (id) }
                ?.map { mapToExecution(it) }.singleOrNull()
        }
    }

    fun createExecution(createExecution: CreateExecution): UUID {
        return transaction {
            DBExecution.insert {
                it[id]                  = UUID.randomUUID()
                it[startdate]           = createExecution.startDate.toDateTime()
                it[enddate]             = createExecution.endDate.toDateTime()
                it[executionTypeUuid]   = createExecution.executionTypeUuid
            } get DBExecution.id
        }
    }

    fun getExecutionResult(executionResultId: UUID): ExecutionResult? {
        // TODO: should I cache this stock
        return DBExecutionResult.select{DBExecutionResult.id eq (executionResultId)}
            ?.map { mapToExecutionResult(it) }.singleOrNull()
    }

    fun createExecutionResult(createExecutionResult: CreateExecutionResult, executionStock: ExecutionStock): UUID {
        // make sure stock exists
        getExecutionStock(executionStock.id)?:getExecutionStock(createExecutionStock(executionStock))

        return DBExecutionResult.insert {
            it[id]                   = UUID.randomUUID()
            it[executionId]          = createExecutionResult.executionId
            it[stockId]              = executionStock.id
            it[executionResultName]  = createExecutionResult.name.name
            it[executionResultValue] = createExecutionResult.value
        } get DBExecutionResult.id
    }

    private fun getExecutionStock(stockId: UUID): ExecutionStock? {
        // TODO: should I cache this stock
        return DBStock.select{DBStock.id eq (stockId)}
            ?.map { mapToExecutionStock(it) }.singleOrNull()
    }

    private fun createExecutionStock(executionStock: ExecutionStock): UUID {
        // check if stockmarket exists
        return transaction{
            getStockMarket(executionStock.stockMarketId)?:createStockMarket(executionStock.stockMarketId)

            DBStock.insert {
                it[id]                  = executionStock.id
                it[name]                = executionStock.name
                it[ticker]              = executionStock.ticker
                it[stockMarketId]       = executionStock.stockMarketId
            } get DBStock.id
        }
    }

    private fun getStockMarket(stockMarketId: UUID): ExecutionStockMarket? {
        return DBExecutionStockMarket.select{ DBExecutionStockMarket.id eq (stockMarketId) }
            ?.map { mapToStockMarket(it) }.singleOrNull()
    }

    private fun createStockMarket(stockMarketId: UUID): UUID {
        val executionStockMarket = stockMarketRepository.get(stockMarketId).asExecutionStockMarket()
        return transaction {
            DBExecutionStockMarket.insert {
                it[id]                  = executionStockMarket.id
                it[name]                = executionStockMarket.name
            } get DBExecutionStockMarket.id
        }
    }

    fun mapToExecutionType(it: ResultRow) = ExecutionAlgorithm(
        id   = it[DBExecutionType.id],
        name = ExecutionAlgorithmName.valueOf(it[DBExecutionType.name])
    )

    fun mapToExecutionResult(it: ResultRow) = ExecutionResult(
        id          = it[DBExecutionResult.id],
        name        = ExecutionResultName.valueOf(it[DBExecutionResult.executionResultName]),
        value       = it[DBExecutionResult.executionResultValue],
        executionId = it[DBExecutionResult.executionId],
        stockId     = it[DBExecutionResult.stockId]
    )

    fun mapToExecutionStock(row: ResultRow):ExecutionStock {
        return ExecutionStock(
            id              = row[DBStock.id],
            name            = row[DBStock.name],
            ticker          = row[DBStock.ticker],
            stockMarketId   = row[DBStock.stockMarketId],
        )
    }

    fun mapToStockMarket(row: ResultRow):ExecutionStockMarket {
        return ExecutionStockMarket(
            id      = row[DBStock.id],
            name    = row[DBStock.name]
        )
    }

    fun mapToExecution(row: ResultRow):Execution {
        return Execution(
            id                  = row[DBExecution.id],
            startDate           = row[DBExecution.startdate].toJavaLocalDate(),
            endDate             = row[DBExecution.enddate].toJavaLocalDate(),
            executionTypeUuid   = row[DBExecution.executionTypeUuid],
        )
    }

    fun LocalDate.toDateTime(): DateTime{
        return DateTime(DateTimeZone.UTC)
            .withDate(year, monthValue, dayOfMonth)
            .withTime(0, 0, 0, 0);
    }

    fun DateTime.toJavaLocalDate(): LocalDate {
        return LocalDate.of(this.year().get(), this.monthOfYear().get(), this.dayOfMonth().get())
    }

}