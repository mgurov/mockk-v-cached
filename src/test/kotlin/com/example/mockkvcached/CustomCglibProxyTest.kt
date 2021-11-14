package com.example.mockkvcached

import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class CustomCglibProxyTest {

    @Test
    fun shallProxyCustomly() {
        val toProxy = Service(prefix = "who ")

        val proxied =  proxy(toProxy)

        assertThat(proxied.respondCached("what is the meaning of life?")).startsWith("42 who what")
    }

    private fun proxy(toProxy: Service): Service {
        val enhancer = Enhancer()
        enhancer.setSuperclass(Service::class.java)
        enhancer.setCallback(MethodInterceptor { obj, method, args, proxy ->
            "42 " +  proxy.invokeSuper(obj, args)
        } as MethodInterceptor? )
        val proxy: Service = enhancer.create() as Service
        return proxy
    }

}