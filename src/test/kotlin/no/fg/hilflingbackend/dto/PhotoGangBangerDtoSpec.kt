
import no.fg.hilflingbackend.dto.RelationshipStatus
import org.mockito.internal.util.Platform.describe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PhotoGangBangerDtoSpec : Spek({

  describe("PhotoGangBangerDto") {
    describe("RelationshitStatus ENUM") {
      it("Can create enum from string") {
        val status = RelationshipStatus.valueOf("single")
        assertEquals(RelationshipStatus.single, status)
        assertEquals(RelationshipStatus.married, RelationshipStatus.valueOf("married"))
        assertEquals(RelationshipStatus.relationship, RelationshipStatus.valueOf("relationship"))
      }
      it("Fail on not a enum") {
        assertFailsWith<IllegalArgumentException> {
          RelationshipStatus.valueOf("Alene")
        }
      }
    }
    // TODO: Test SemesterStart
    // TODO : Test PhotoGangBangerDto
  }
})
