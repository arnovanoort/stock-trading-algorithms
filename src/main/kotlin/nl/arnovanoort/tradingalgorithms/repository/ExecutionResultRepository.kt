package nl.arnovanoort.tradingalgorithms.repository

import nl.arnovanoort.tradingalgorithms.domain.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional

@Repository
@Transactional
class ExecutionResultRepository {

    @Autowired
    private lateinit var stockMarketRepository: StockMarketRepository;

    var logger: Logger = LoggerFactory.getLogger(ExecutionResultRepository::class.java)

    fun getExecutiontypeByName(executionName: String): ExecutionAlgorithm?{
        // TODO: cache executiontype
        return DBExecutionType.select {DBExecutionType.name eq executionName}
            .map { mapToExecutionType(it) }
            .firstOrNull()
    }

    fun getExecution(id: UUID): Execution? {
        return DBExecution
            .select { DBExecution.id eq (id) }
            ?.map { mapToExecution(it) }.singleOrNull()
    }

    fun createExecution(createExecution: CreateExecution): UUID {
       return  DBExecution.insert {
            it[id]                  = UUID.randomUUID()
            it[startdate]           = createExecution.startDate.toDateTime()
            it[enddate]             = createExecution.endDate.toDateTime()
            it[executionTypeUuid]   = createExecution.executionTypeUuid
        } get DBExecution.id
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
        val stockmarket = getStockMarket(executionStock.stockMarketId)?:createStockMarket(executionStock.stockMarketId)

        return DBStock.insert {
            it[id]                  = executionStock.id
            it[name]                = executionStock.name
            it[ticker]              = executionStock.ticker
            it[stockMarketId]       = executionStock.stockMarketId
        } get DBStock.id
    }

    private fun getStockMarket(stockMarketId: UUID): ExecutionStockMarket? {
        return DBExecutionStockMarket.select{ DBExecutionStockMarket.id eq (stockMarketId) }
            ?.map { mapToStockMarket(it) }.singleOrNull()
    }

    private fun createStockMarket(stockMarketId: UUID): UUID {
        val executionStockMarket = stockMarketRepository.get(stockMarketId).asExecutionStockMarket()
        return DBExecutionStockMarket.insert {
            it[id]                  = executionStockMarket.id
            it[name]                = executionStockMarket.name
        } get DBExecutionStockMarket.id
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
            id      = row[DBExecutionStockMarket.id],
            name    = row[DBExecutionStockMarket.name]
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