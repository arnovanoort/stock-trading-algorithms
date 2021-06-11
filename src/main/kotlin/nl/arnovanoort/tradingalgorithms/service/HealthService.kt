package nl.arnovanoort.tradingalgorithms.service

import nl.arnovanoort.tradingalgorithms.domain.Health
import nl.arnovanoort.tradingalgorithms.repository.HealthRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HealthService {

    @Autowired
    private lateinit var healthRepository: HealthRepository

    fun check(): Health {
        return healthRepository.getHealth()
    }

}