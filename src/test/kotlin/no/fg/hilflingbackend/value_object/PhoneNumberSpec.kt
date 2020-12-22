import no.fg.hilflingbackend.value_object.PhoneNumber

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PhoneNumberSpec : Spek({

    describe("PhoneNumber") {
      mapOf(
        "96518405" to "96518405",
      ).forEach { input, expected ->
          describe("Testing input $input"){
            it("Correctly returns $expected"){
              assertEquals(expected, PhoneNumber(input).value)
            }
          }
      }
      it("Fails on not a phonenumber") {
        assertFailsWith<IllegalArgumentException>{
          PhoneNumber("Aakjre")
        }
      }
    }
})
