package hilfling.backend.hilfing.demodata

import hilfling.backend.hilfing.model.Album
import hilfling.backend.hilfing.model.Category
import hilfling.backend.hilfing.service.AlbumService
import hilfling.backend.hilfing.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

import java.util.ArrayList

@Component
class SeedAlbums (val service: AlbumService){

    @EventListener
    fun seed(event: ApplicationReadyEvent) {
        val num_of_albums = 12
        for (i in 0 until num_of_albums) {
            service.create(Album(String.format("Album nr: %d", i), false))
        }
    }
}
