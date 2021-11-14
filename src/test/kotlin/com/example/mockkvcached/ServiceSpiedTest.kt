package com.example.mockkvcached

import io.mockk.every
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.aop.framework.Advised
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@SpringBootTest
class ServiceCachedSpiedTest(
    @Autowired private val service: Service
) {
    @Test
    fun `this fails because cached`() {

        every { service.respondCached("empty") } returns ""

        assertThat(service.respondCached("empty")).isNotNull()
    }
}

@SpringBootTest
class MockkSpykingTest(
    @Autowired private val spiedService: Service
) {
    @Test
    fun `should be able to invoke Target directly`() {

        every { spiedService.respondCached("empty") } returns ""

        val firstUnwrap = (spiedService as Advised).targetSource.target
        val secondUnwrap = (firstUnwrap as Advised).targetSource.target

        SoftAssertions.assertSoftly {
            it.assertThat(secondUnwrap).isNotInstanceOf(Advised::class.java)
            it.assertThat(secondUnwrap).isInstanceOf(Service::class.java)
//            it.assertThat(secondUnwrap).isInstanceOf(Service::class.java)
        }

    }

}

private fun unroll(source: Any) {
    var nextStep = source
    while (true) {

        val targetSource = when (nextStep) {
            is Advised -> {
                println("Unadvising $nextStep")
                nextStep.targetSource
            }
            else -> break
        }

        nextStep = targetSource.target!!
    }

    println("Unwrapped to $nextStep")
}


@Configuration
class InjectSpiesConfiguration {
    @Bean
    @Primary
    fun spiedService(service: Service): Service {
//        unroll(service)
        val spyk = spyk(service)
        println("wrapping to $spyk")
        //spyk.respondCached("what's the meaning of life")
        //every { spyk.respondCached("empty") } returns ""
//        unroll(spyk)
        return spyk
    }

    // workaround 2: use unwrapped beans to spy
//    @Bean
//    @Primary
//    fun spiedUnwrappedService(service: Service): Service {
//        val actualService: Service = AopTestUtils.getUltimateTargetObject(service)
//        val spyk = spyk(actualService)
//        return spyk
//    }
}