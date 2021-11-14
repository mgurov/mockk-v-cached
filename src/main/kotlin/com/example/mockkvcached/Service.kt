package com.example.mockkvcached

import org.springframework.stereotype.Component
import java.time.Instant

@Component
class Service(
    val prefix: String = ""
) {
    fun respond(input: String) = prefix + input + "_" + Instant.now().toEpochMilli()
}

@Component
class DependentService(
    val service: Service,
) {
    fun doubleRespond(input: String) = service.respond(input) + " v " + service.respond(input)
}