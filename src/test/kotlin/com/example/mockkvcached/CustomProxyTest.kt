package com.example.mockkvcached

import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.aop.TargetSource
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

    @Test
    fun addOneSpyke() {
        val toProxy = Service()

        val proxied = proxy(toProxy)

        val spyked = spyk(proxied)

        assertThat(spyked.respondCached("what is the meaning of life?")).startsWith("42 what")
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

interface MyServiceAdviced {
    fun getTargetSource(): MyTargetSource
}

class DynamicInvocationHandler(
    proxied: ServiceInterface
) : InvocationHandler, MyServiceAdviced {

    private val myTargetSource = MyTargetSource(proxied)

    override operator fun invoke(proxy: Any?, method: Method, args: Array<Any?>): Any {
        LOGGER.info("Invoked method: {} with params {}", method.getName(), args)
        return "42 " + method.invoke(myTargetSource.target, *args)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(
            DynamicInvocationHandler::class.java
        )
    }

    override fun getTargetSource(): MyTargetSource {
        return myTargetSource
    }
}

class MyTargetSource(
    val target: ServiceInterface
) : TargetSource {
    override fun getTargetClass(): Class<*>? {
        return target.javaClass::class.java
    }

    override fun isStatic(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTarget(): Any? {
        return target
    }

    override fun releaseTarget(target: Any) {
        TODO("Not yet implemented")
    }
}