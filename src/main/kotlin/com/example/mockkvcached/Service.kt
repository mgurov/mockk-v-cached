package com.example.mockkvcached

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class Service(
    val prefix: String = ""
) {

    @Cacheable("response_cache")
    fun respondCached(input: String) = prefix + input + "_" + Instant.now().toEpochMilli()

}