package com.example.mockkvcached

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import io.mockk.every
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
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

@Configuration
class InjectSpiesConfiguration{
    @Bean
    @Primary
    fun spiedService(service: Service): Service {
        return spyk(service)
    }

    // workaround 2: use unwrapped beans to spy
    @Bean
    @Primary
    fun spiedUnwrappedService(service: Service): Service {
        val actualService: Service = AopTestUtils.getUltimateTargetObject(service)
        val spyk = spyk(actualService)
        return spyk
    }
}