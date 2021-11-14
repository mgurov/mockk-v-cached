package com.example.mockkvcached

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.time.Instant


interface ServiceInterface {
    fun respondCached(input: String): String
}

@Component
class Service: ServiceInterface {

    @Cacheable("response_cache")
    override fun respondCached(input: String) = input + "_" + Instant.now().toEpochMilli()

}