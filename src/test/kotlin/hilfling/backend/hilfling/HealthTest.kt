package hilfling.backend.hilfling

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

//@RunWith(SpringRunner::class)
//@SpringBootTest(webEnvironment =
//        SpringBootTest.WebEnvironment.RANDOM_PORT
//)
//class HealthTest {
//    @Autowired
//    lateinit var testRestTemplate: TestRestTemplate
//
//
//    @Test
//    fun testHealthController() {
//        val result = testRestTemplate.getForEntity("/health", String::class.java)
//        assertNotNull(result)
//        assertEquals(result.statusCode, HttpStatus.OK)
//        assertEquals(result.body, "Status: ok!")
//    }
//}