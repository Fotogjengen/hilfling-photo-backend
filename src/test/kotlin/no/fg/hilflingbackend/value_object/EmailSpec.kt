// package no.fg.hilflingbackend.value_object
// import org.junit.jupiter.api.assertThrows
// import org.spekframework.spek2.Spek
// import org.spekframework.spek2.style.specification.describe
// import kotlin.test.assertEquals

// class EmailSpec : Spek(
//   {
//     describe("Create Email test") {
//       it("Should fail on not email") {
//         assertThrows<IllegalArgumentException> { Email("this is not an email") }
//       }
//       it("Should succeed on email") {
//         val email = Email("sindresinemail@gmail.com")
//         assertEquals(email.value, "sindresinemail@gmail.com")
//       }
//       it("Works with all domains") {
//         val emailString = "a@a.com"
//         assertThrows<IllegalArgumentException> { Email("this is not an email") }
//       }
//       it("Works with + sign") {
//         // TODO: This should work. Fix Regex
//         // val emailString = "carolinesanbraten+komplett@gmail.com"
//         // val email = Email.create(emailString)
//         // assertEquals(email.value, emailString)
//       }
//     }
//   }
// )
