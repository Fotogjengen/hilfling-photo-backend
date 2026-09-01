import no.fg.hilflingbackend.dto.PhotoGangBangerId
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertNotEquals

class PhotoGangBangerDtoSpec :
  Spek({
    describe("PhotoGangBangerDto") {
      // TODO: Test SemesterStart
      // TODO: Test PhotoGangBangerDto

      it("generates a PhotoGangBangerId when input id is empty") {
        val first = PhotoGangBangerId("")
        val second = PhotoGangBangerId("")

        assertNotEquals(first.id, second.id)
      }
    }
  })
