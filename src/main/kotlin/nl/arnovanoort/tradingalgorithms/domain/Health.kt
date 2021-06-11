package nl.arnovanoort.tradingalgorithms.domain

import java.util.*

data class Health (
    val status: List<HealthStatus>
) {
    fun isOK(): Boolean{
        return status.size == 1 && status[0] == HealthStatus.OK
    }
}

enum class HealthStatus{
    OK,
    STOCK_READER_ERROR
}