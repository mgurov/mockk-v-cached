package com.example.mockkvcached

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
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.util.AopTestUtils

@SpringBootTest
// Don't use DirtiesContext in production tests, this is just to workaround unclean spies reset for this particular issue demonstration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MockkSpykingTest(
    @Autowired private val spiedService: Service
) {

    @BeforeEach
    fun `reset mocks`() {
        //NB: no failures when the following line is commented.
        every { spiedService.respondCached("empty") } returns ""
    }

    @Test
    fun `hardcoded unwrapping`() {

        val firstUnwrap = (spiedService as Advised).targetSource.target
        val secondUnwrap = (firstUnwrap as Advised).targetSource.target

        SoftAssertions.assertSoftly {
            it.assertThat(secondUnwrap).isNotInstanceOf(Advised::class.java)
            it.assertThat(secondUnwrap).isInstanceOf(Service::class.java)
        }

    }

    @Test
    fun `this is the call that fails for spring mockito ResetMocksTestExecutionListener`() {
        val unwrapped = AopTestUtils.getUltimateTargetObject<Any>(spiedService)
        SoftAssertions.assertSoftly {
            it.assertThat(unwrapped).isNotInstanceOf(Advised::class.java)
            it.assertThat(unwrapped).isInstanceOf(Service::class.java)
        }
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