package no.fg.hilflingbackend

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HilflingApplicationTests {

  @BeforeAll
  fun setup() {
    print(">> Setup")
  }

  @Test
  fun contextLoads() {
    // error("Force failure")
  }

  @AfterAll
  fun teardown() {
    println(">> Teardown")
  }
}
