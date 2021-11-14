package com.example.mockkvcached

import org.slf4j.LoggerFactory
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
        private val logger = LoggerFactory.getLogger(UnrelatedLongStartingService::class.java)
    }
}