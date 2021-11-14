package com.example.mockkvcached

import io.mockk.spyk
import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class CustomCglibProxyTest {

    @Test
    fun shallProxyCustomly() {
        val toProxy = Service(prefix = "who ")

        val proxied = MyCglibEnhancer(toProxy).proxy()

        assertThat(proxied.respondCached("what is the meaning of life?")).startsWith("42 who what")
    }

    @Test
    fun oneLevelOfSpykeness() {
        val toProxy = Service(prefix = "who ")

        val proxied = MyCglibEnhancer(toProxy).proxy()

        val spyked = spyk(proxied)

        assertThat(spyked.respondCached("what is the meaning of life?")).startsWith("42 who what")
    }


}

class MyCglibEnhancer(
    target: Service
): MyServiceAdviced {
    private val myTargetSource = MyTargetSource(target)

    override fun getTargetSource(): MyTargetSource {
        return myTargetSource
    }

    fun proxy(): Service {
        val enhancer = Enhancer()
        enhancer.setSuperclass(Service::class.java)
        enhancer.setCallback(MethodInterceptor { obj, method, args, proxy ->
            "42 " +  method.invoke(myTargetSource.target, *args)
        } as MethodInterceptor? )
        val proxy: Service = enhancer.create() as Service
        return proxy
    }
}