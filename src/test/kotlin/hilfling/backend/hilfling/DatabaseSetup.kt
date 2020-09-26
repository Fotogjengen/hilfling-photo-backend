package hilfling.backend.hilfling

import hilfling.backend.hilfling.model.Album
import hilfling.backend.hilfling.model.ArticleTags
import hilfling.backend.hilfling.repository.AlbumRepository
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.logging.ConsoleLogger
import me.liuwj.ktorm.logging.LogLevel
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource



//open class BaseTest {
//    @Before
//    open fun init() {
//        Database.connect(
//            url = "jdbc:h2:mem:ktorm;DB_CLOSE_DELAY=-1",
//            driver = "org.h2.Driver",
//            logger = ConsoleLogger(threshold = LogLevel.TRACE)
//        )
//
//        // Populate database with data
//        // execSqlScript("init-data.sql")
//    }
//
//    @After
//    open fun destroy() {
//        //execSqlScript("drop-data.sql")
//    }
//}
//class AppSpec: BaseTest() {
//
//    companion object {
//        class AppPosgresSqlContainer : PostgreSQLContainer<AppPosgresSqlContainer>("postgres:12")
//        @ClassRule
//        @JvmField
//        val postgres = AppPosgresSqlContainer()
//                .withDatabaseName("hilflingdb")
//                .withUsername("hilfling")
//                .withPassword("password")
//    }
//
//    override fun init() {
//        Database.connect(
//                url = postgres.jdbcUrl,
//                driver = postgres.driverClassName,
//                user = postgres.username,
//                password = postgres.password,
//                logger = ConsoleLogger(threshold = LogLevel.TRACE)
//        )
//    }
//
//
//    @Test
//    fun firsTest() {
//        val albumRepository = AlbumRepository()
//        val album = Album {
//            isAnalog = false;
//            title = "test";
//        }
//        albumRepository.create(album.title, isAnalog = false)
//
//
//
//    }
//
//}