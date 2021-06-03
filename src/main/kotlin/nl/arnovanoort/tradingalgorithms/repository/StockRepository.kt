package nl.arnovanoort.tradingalgorithms.repository

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import khttp.get
import khttp.responses.Response
import org.springframework.beans.factory.annotation.Value
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import nl.arnovanoort.tradingalgorithms.domain.Stock
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import nl.arnovanoort.tradingalgorithms.domain.StockPrice
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Repository
class StockRepository {

    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @Value("\${stockdata.url.stocks}")
    lateinit var getStocksUrl: String;

    @Value("\${stockdata.url.stockprices}")
    lateinit var getStockPricesUrl: String;

    fun getStocks(stockMarketUuid: UUID): List<Stock> {
        val stockResponse: Response = get(getStocksUrl)
        return objectMapper.readValue(stockResponse.jsonArray.toString())
    }

    fun getStockPrice(id: UUID, startDate: LocalDate, endDate: LocalDate): List<StockPrice> {
        val url = getStockPricesUrl
                .replace("<uuid>", id.toString())
                .replace("{startDate}", startDate.format(dateFormatter))
                .replace("{endDate}", endDate.format(dateFormatter))
        val stockResponse: Response = get(url)
        return objectMapper.readValue(stockResponse.jsonArray.toString())
    }

    companion object{
        val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
    }
}