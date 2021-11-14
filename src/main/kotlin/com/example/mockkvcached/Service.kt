package com.example.mockkvcached

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.time.Instant


interface ServiceInterface {
    fun respondCached(input: String): String
}

@Component
class Service(
    val prefix: String = ""
): ServiceInterface {

    @Cacheable("response_cache")
    override fun respondCached(input: String) = prefix + input + "_" + Instant.now().toEpochMilli()

}