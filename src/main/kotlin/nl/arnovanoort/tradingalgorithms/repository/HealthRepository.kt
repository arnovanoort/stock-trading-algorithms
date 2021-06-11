package nl.arnovanoort.tradingalgorithms.repository

import khttp.get
import nl.arnovanoort.tradingalgorithms.domain.Health
import nl.arnovanoort.tradingalgorithms.domain.HealthStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class HealthRepository {

    @Value("\${stockdata.url.host}")
    lateinit var stockDataHealthHost: String;

    @Value("\${stockdata.url.health}")
    lateinit var stockDataHealthUrl: String;

    fun getHealth(): Health {
        val uri = stockDataHealthHost + stockDataHealthUrl
        val stockResponse = get(uri).statusCode
        return if(stockResponse == 200) {
            Health(listOf(HealthStatus.OK))
        } else {
            Health(listOf(HealthStatus.STOCK_READER_ERROR))
        }
    }
}