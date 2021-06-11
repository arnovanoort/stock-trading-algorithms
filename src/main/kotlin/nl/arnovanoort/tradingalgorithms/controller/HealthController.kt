package nl.arnovanoort.tradingalgorithms.controller

import nl.arnovanoort.tradingalgorithms.service.HealthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @Autowired
    private lateinit var healthService: HealthService

    @GetMapping("/health")
    fun health():ResponseEntity<String>{
        if( healthService.check().isOK() ){
            return ResponseEntity(HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}