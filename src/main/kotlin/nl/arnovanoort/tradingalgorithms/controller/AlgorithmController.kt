package nl.arnovanoort.tradingalgorithms.controller

import nl.arnovanoort.tradingalgorithms.implementation.MomentumAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AlgorithmController {

    @Autowired
    private lateinit var momentumAlgorithm: MomentumAlgorithm

    @PostMapping("/algorithms")
    fun executeAlgorithm(request: ExecuteAlgorithmRequest){
        momentumAlgorithm.execute(request.stockMarketId, request.params)
    }
}

data class ExecuteAlgorithmRequest(
    val stockMarketId: UUID,
    val params: HashMap<String,String>
)