package hilfling.backend.hilfing.demodata

import ch.qos.logback.core.util.Loader
import hilfling.backend.hilfing.model.User
import hilfling.backend.hilfing.service.UserService
import org.aspectj.weaver.loadtime.Aj
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileNotFoundException
import javax.annotation.Resource
import org.springframework.core.io.ClassPathResource
import kotlin.random.Random


@Component
class SeedUsers (val userService: UserService) {

    val names: List<String> = this.readNamesFromFile("static/first-names.txt")

    @EventListener
    fun seed(event: ApplicationReadyEvent) {
        val numberOfUsers = 20;
        repeat(numberOfUsers) {
            val user: User = User(
                names.random(),
                    generateLastName(names.random()),
                    names.random(),
                    generateEmail(),
                    "https://picsum.photos/200",
                    generateNumber(),
                    generateGender()
            )
            println(user)
            userService.create(user)
        }
    }

    private fun generateGender(): String {
        return if (Random.nextBoolean()) "Mann" else "Kvinne"
    }
    private  fun generateLastName(firstname: String): String {
        return firstname + "esen"
    }

    private fun generateEmail(): String {
        val charPol: List<Char> = ('a'..'z') + ('0'..'9')
        val stringLength: Int = 5
        var email: String = (0..stringLength)
                .map{kotlin.random.Random.nextInt(0, charPol.count())}
                .map(charPol::get)
                .joinToString("") + "@gmail.com"
        return email
    }
    private fun generateNumber(): String {
        var number: String = ""
        for (n in 0  until 8){
            number += Random(12).nextInt(0, 9).toString()
        }
        return number
    }
    private fun readNamesFromFile(filename: String): List<String> {
        val resource = ClassPathResource(filename)
        try {
            val file: File = File(resource.uri)
            return file.readLines()
        } catch (e: FileNotFoundException) {
            print(e)
            return emptyList()
        }
    }
}