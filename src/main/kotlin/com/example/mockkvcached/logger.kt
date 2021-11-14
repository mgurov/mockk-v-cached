package com.example.mockkvcached

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Any.logger(): Logger {
    val clazz = if (this::class.isCompanion) this::class.java.enclosingClass else this::class.java
    return LoggerFactory.getLogger(clazz)
}
