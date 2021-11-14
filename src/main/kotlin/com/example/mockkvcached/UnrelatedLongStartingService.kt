package com.example.mockkvcached

import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class UnrelatedLongStartingService {
    init {
        logger.info("Starting>>>")
        TimeUnit.SECONDS.sleep(1)
        logger.info(">>>Started")
    }

    companion object {
        private val logger = logger()
    }
}