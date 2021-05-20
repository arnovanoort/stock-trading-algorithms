package nl.arnovanoort.tradingalgorithms.domain

import nl.arnovanoort.tradingalgorithms.MomentumException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class Execution(
    val id                  : UUID,
    val startDate           : LocalDate,
    val endDate             : LocalDate,
    val executionTypeUuid   : UUID
)

data class CreateExecution(
    val startDate           : LocalDate,
    val endDate             : LocalDate,
    val executionTypeUuid   : UUID
){

    companion object{
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        fun fromParams(executionTypeUuid: UUID, params: Map<String,String>): CreateExecution{
            val endDate = params.get("endDate")
                ?.let{ enddate -> LocalDate.parse(enddate, dateFormatter)}
                ?:LocalDate.now()

            val startDate = params
                .get("lookback")
                ?.toLong()
                ?.let { lookback -> endDate.minusDays(lookback) }
                ?:throw(MomentumException("Lookback variable is required for momentum algorithm."))

            return CreateExecution(
                startDate,
                endDate,
                executionTypeUuid
            )
        }
    }

}

