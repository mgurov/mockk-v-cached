package com.example.mockkvcached

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
import org.springframework.test.util.AopTestUtils
import java.util.*

@SpringBootTest
class MockkSpykingTest(
    @Autowired private val spiedService: Service
) {

    @Test
    fun `service should work`() {
        val givenRandom = UUID.randomUUID().toString()

        assertThat(spiedService.respond(givenRandom)).matches("${givenRandom}_(\\d+)")
    }
}

@SpringBootTest
class DependentServiceTest(
    val dependentService: DependentService,
) {
    @Test
    fun `shall handle explosions nicely`() {
        assertThat(dependentService.doubleRespond("blah")).matches("blah _(\\d+) v blah _(\\d+)")
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