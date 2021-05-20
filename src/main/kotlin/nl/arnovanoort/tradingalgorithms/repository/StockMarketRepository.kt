package nl.arnovanoort.tradingalgorithms.repository

import java.util.*
import khttp.get
import khttp.responses.Response
import org.springframework.beans.factory.annotation.Value
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import nl.arnovanoort.tradingalgorithms.domain.Stock
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import nl.arnovanoort.tradingalgorithms.domain.StockMarket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class StockMarketRepository {

    @Value("\${stockdata.url.stocks}")
    lateinit var stockDataUrl: String;

    @Value("\${stockdata.url.stockmarket}")
    lateinit var stockMarketDataUrl: String;

    val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    fun getStocks(stockMarketUuid: UUID): List<Stock> {
        val stockResponse = get(stockDataUrl).jsonArray.toString()
        return mapper.readValue(stockResponse)
    }

    fun get(stockMarketUuid: UUID): StockMarket {
        val stockMarketResponse: String = get(stockMarketDataUrl).jsonArray.toString()
        return mapper.readValue(stockMarketResponse)
    }
}