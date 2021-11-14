package com.example.mockkvcached

import io.mockk.every
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.Test
import org.springframework.aop.framework.Advised
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.test.util.AopTestUtils

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
    fun `should be able to invoke Target`() {

        every { spiedService.respondCached("empty") } returns ""

        //AopTestUtils.getUltimateTargetObject<Any>(spiedService)

        var nextStep: Any = spiedService

        while (true) {

            val targetSource = when(nextStep) {
                is Advised -> {
                    println("Unadvising $nextStep")
                    nextStep.targetSource
                }
                else -> break
            }

            nextStep = targetSource.target!!
        }


    }
}

@Configuration
class InjectSpiesConfiguration {
    @Bean
    @Primary
    fun spiedService(service: Service): Service {
        return spyk(service)
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