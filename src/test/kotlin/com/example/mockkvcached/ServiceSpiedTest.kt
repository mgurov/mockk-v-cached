package com.example.mockkvcached

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.spyk
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.aop.framework.Advised
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@SpringBootTest
class MockkSpykingTest(
    @Autowired private val spiedService: Service
) {

    @BeforeEach
    fun `resetMocks`() {
        clearAllMocks()
    }

    @Test
    fun `will fail because of spy pre-config`() {

        every { spiedService.respondCached("empty") } returns ""

        getTargetTwice()

    }

    @Test
    fun `won't fail cause no pre-interaction with spy`() {

        //every { spiedService.respondCached("empty") } returns ""

        getTargetTwice()
    }

    private fun getTargetTwice() {
        val firstUnwrap = (spiedService as Advised).targetSource.target
        val secondUnwrap = (firstUnwrap as Advised).targetSource.target

        SoftAssertions.assertSoftly {
            it.assertThat(secondUnwrap).isNotInstanceOf(Advised::class.java)
            it.assertThat(secondUnwrap).isInstanceOf(Service::class.java)
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
        val spyk = spyk(service)
        println("wrapping to $spyk")
        return spyk
    }

    // workaround: use unwrapped beans to spy
//    @Bean
//    @Primary
//    fun spiedService(service: Service): Service {
//        val actualService: Service = AopTestUtils.getUltimateTargetObject(service)
//        val spyk = spyk(actualService)
//        return spyk
//    }
}