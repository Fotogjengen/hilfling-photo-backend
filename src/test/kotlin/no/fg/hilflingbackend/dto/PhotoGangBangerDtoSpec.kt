import com.fasterxml.jackson.databind.ObjectMapper
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
        val mapper = ObjectMapper()
        val first = mapper.readValue("""{"id": ""}""", PhotoGangBangerId::class.java)
        val second = mapper.readValue("""{"id": ""}""", PhotoGangBangerId::class.java)

        assertNotEquals(first.id, second.id)
      }
    }
  })
