package nl.arnovanoort.tradingalgorithms.repository

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object DBExecutionType: Table("execution_type"){
    val id                  = uuid("id").primaryKey()
    val name                = varchar("name", 36)
}

object DBExecution: Table("execution") {
    val id                  = uuid("id").primaryKey()
    val startdate           = date("startdate")
    val enddate             = date("enddate")
    val executionTypeUuid   = uuid("execution_type_id").references(DBExecutionType.id, onDelete = ReferenceOption.CASCADE)
}

object DBExecutionStockMarket: Table("stock_market"){
    val id                  = uuid("id").primaryKey()
    val name                = varchar("name", 32)
}

object DBStock: Table("stock"){
    val id                  = uuid("id").primaryKey()
    val name                = varchar("name", 32)
    val ticker              = varchar("ticker",10)
    val stockMarketId       = uuid("stock_market_id").references(DBExecutionStockMarket.id)
}

object DBExecutionResult: Table("execution_result"){
    val id                  = uuid("id").primaryKey()
    val executionId         = uuid("execution_id").references(DBExecution.id, onDelete = ReferenceOption.CASCADE)
    val stockId             = uuid("stock_id").references(DBStock.id, onDelete = ReferenceOption.CASCADE)
    val executionResultName = varchar("execution_result_name", 36)
    val executionResultValue= decimal("execution_result_value", 10,2)
}