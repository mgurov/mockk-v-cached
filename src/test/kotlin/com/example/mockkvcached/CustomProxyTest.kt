package com.example.mockkvcached

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class CustomProxyTest {


    @Test
    fun shallProxyCustomly() {
        val toProxy = Service()

        val proxied = proxy(toProxy)

        assertThat(proxied.respondCached("what is the meaning of life?")).startsWith("42 what")
    }

    private fun proxy(toProxy: Service): ServiceInterface {
        val proxied = Proxy.newProxyInstance(
            this.javaClass.classLoader,
            arrayOf(ServiceInterface::class.java),
            DynamicInvocationHandler(toProxy)
        ) as ServiceInterface
        return proxied
    }

}

class DynamicInvocationHandler(
    val proxied: ServiceInterface
) : InvocationHandler {
    override operator fun invoke(proxy: Any?, method: Method, args: Array<Any?>): Any {
        LOGGER.info("Invoked method: {} with params {}", method.getName(), args)
        return "42 " + method.invoke(proxied, *args)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(
            DynamicInvocationHandler::class.java
        )
    }
}