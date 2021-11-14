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
import org.springframework.test.util.AopTestUtils

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

    @Test
    fun `this is the call that fails for spring mockito ResetMocksTestExecutionListener`() {
        every { spiedService.respondCached("empty") } returns ""
        AopTestUtils.getUltimateTargetObject<Any>(spiedService)
    }

}

@Configuration
class InjectSpiesConfiguration {
    @Bean
    @Primary
    fun spiedService(service: Service): Service {
        val spyk = spyk(service)
        return spyk
    }
}