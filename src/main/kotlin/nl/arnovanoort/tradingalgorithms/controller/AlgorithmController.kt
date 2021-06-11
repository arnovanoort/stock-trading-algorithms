package nl.arnovanoort.tradingalgorithms.controller

import nl.arnovanoort.tradingalgorithms.implementation.MomentumAlgorithm
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AlgorithmController {

    var logger: Logger = LoggerFactory.getLogger(AlgorithmController::class.java)

    @Autowired
    private lateinit var momentumAlgorithm: MomentumAlgorithm

    @PostMapping("/algorithms")
    fun executeAlgorithm(@RequestBody request: ExecuteAlgorithmRequest){
        logger.debug("processing request " + request)
        momentumAlgorithm.execute(request.uuid, request.params)
    }
}

data class ExecuteAlgorithmRequest(
        val uuid: UUID,
        val params: Map<String,String>
)