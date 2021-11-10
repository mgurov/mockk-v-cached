package com.example.mockkvcached

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class MockkVCachedApplication

fun main(args: Array<String>) {
	runApplication<MockkVCachedApplication>(*args)
}
