package com.example.mockkvcached

import io.mockk.every
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
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
    @Autowired val dependentService: DependentService,
    @Autowired val service: Service,
) {
    @Test
    fun `should work nicely by default`() {
        assertThat(dependentService.doubleRespond("blah")).matches("blah_(\\d+) v blah_(\\d+)")
    }

    @Test
    fun `shall handle explosions nicely`() {

        every { service.respond("blah") } returns "blah_something" andThenThrows IllegalStateException("kaboom")

        assertThat(dependentService.doubleRespond("blah")).matches("blah_something v exploded")
    }

}

@Configuration
class InjectSpiesConfiguration {
    @Bean
    @Primary
    fun spiedService(service: Service): Service {
        return spyk(service)
    }
}


@SpringBootTest
class CountServices(
    @Autowired val services: List<Service>,
) {
    @Test
    fun `should have 2wo instances of Service - one original and one mocked`() {
        assertThat(services).hasSize(2)
    }
}
