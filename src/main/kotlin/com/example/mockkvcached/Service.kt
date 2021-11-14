package com.example.mockkvcached

import org.springframework.stereotype.Component
import java.lang.Exception
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
    fun doubleRespond(input: String) = safely { service.respond(input) } + " v " + safely { service.respond(input) }

    private fun safely(function: () -> String): String {
        return try {
            function()
        } catch (e: Exception) {
            logger().error("we exploded, falling back", e)
            "exploded"
        }
    }

    companion object {
        val logger = logger()
    }
}