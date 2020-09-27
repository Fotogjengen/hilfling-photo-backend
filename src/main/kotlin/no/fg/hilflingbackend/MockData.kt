package no.fg.hilflingbackend

import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.repository.*
import org.springframework.beans.factory.annotation.Autowired

class MockData {
    @Autowired
    val albumRepository = AlbumRepository()
    @Autowired
    val articleRepository = ArticleRepository()
    @Autowired
    val articleTagRepository = ArticleTagRepository()
    @Autowired
    val category = CategoryRepository()
    @Autowired
    val eventOwnerRepository = EventOwnerRepository()
    @Autowired
    val gangRepository = GangRepository()
    @Autowired
    val motiveRepository = MotiveRepository()

    private fun seedAlbumData() {
        listOf(
                Album{
                    title = "Vår 2017";
                    isAnalog = true;
                },
                Album{
                    title = "Høst 2017";
                },
                Album {
                    title = "Vår 2018"
                },
                Album {
                    title = "Høst 2018"
                },
                Album {
                    title = "Vår 2019"
                },
                Album {
                    title = "Høst 2019"
                    isAnalog = true
                }
        ).forEach{
            albumRepository.create(it)
        }

    }
    fun seedMockData () {
        seedAlbumData()
    }


}
