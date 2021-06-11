package nl.arnovanoort.tradingalgorithms.repository

import nl.arnovanoort.tradingalgorithms.domain.Stock
import io.mockk.mockkStatic
import io.mockk.every
import io.mockk.mockk
import khttp.responses.Response
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate
import java.util.*
import org.junit.jupiter.api.Assertions.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBStockMarketRepositoryTest {

    private val stockMarketRepository = StockMarketRepository();

    val stockMarketUuid = UUID.randomUUID()

    @Test
    fun testGetStocks(){

        // data
        val stockMarketUuid = UUID.fromString("af5a2e48-a43f-4ac8-8b0c-24d29477d35b")
        val stockUuid = UUID.randomUUID()
        val today = LocalDate.now()
        val stock = Stock(stockUuid, "Tesla", "TLSA", "stock", "EUR", today, LocalDate.now(), stockMarketUuid)
        val getStocksUrl = "http://replaceWithURL/stockmarket/af5a2e48-a43f-4ac8-8b0c-24d29477d35b/stocks/"

        // prepare
        var responseValue = JSONArray()
        val jsonobject = JSONObject(stock)
        val mockedResponse = responseValue.put(jsonobject)

        val response = mockk<Response>()
        every {response.jsonArray} returns (mockedResponse)

        mockkStatic("khttp.KHttp")
        every { khttp.get(getStocksUrl)} returns response

        // execute
        stockMarketRepository.stockDataUrl = getStocksUrl
        var stocks = stockMarketRepository.getStocks(stockMarketUuid)

        // verify
        assertEquals(stocks, listOf(stock))
    }

}