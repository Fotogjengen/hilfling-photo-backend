package no.fg.hilflingbackend

import com.azure.storage.blob.models.PublicAccessType
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.batchInsert
import no.fg.hilflingbackend.blobStorage.AzureBlobStorage
import no.fg.hilflingbackend.controller.PhotoController
import no.fg.hilflingbackend.dto.*
import no.fg.hilflingbackend.model.Photos
import no.fg.hilflingbackend.repository.*
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.PhoneNumber
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.SecureRandom
import java.time.LocalDate
import java.util.UUID

@Service
class MockDataService {
  @Autowired lateinit var database: Database
  @Autowired lateinit var photoController: PhotoController
  @Autowired lateinit var placeRepository: PlaceRepository
  @Autowired lateinit var albumRepository: AlbumRepository
  @Autowired lateinit var photoGangBangerRepository: PhotoGangBangerRepository
  @Autowired lateinit var positionRepository: PositionRepository
  @Autowired lateinit var samfundetUserRepository: SamfundetUserRepository
  @Autowired lateinit var photoTagRepository: PhotoTagRepository
  @Autowired lateinit var categoryRepository: CategoryRepository
  @Autowired lateinit var eventOwnerRepository: EventOwnerRepository
  @Autowired lateinit var gangRepository: GangRepository
  @Autowired lateinit var motiveRepository: MotiveRepository
  @Autowired lateinit var securityLevelRepository: SecurityLevelRepository
  @Autowired lateinit var photoRepository: PhotoRepository
  @Autowired lateinit var azureBlobStorage: AzureBlobStorage

  fun initializeAzureBlobStorageContainers() {
    listOf("alle", "fggjeng", "husfolk").forEach {
      val containerClient = azureBlobStorage.blobServiceClient.createBlobContainer(it)
      containerClient.setAccessPolicy(PublicAccessType.BLOB, null)
    }
  }

  fun generateSecurityLevelData(): List<SecurityLevelDto> =
    listOf(
      SecurityLevelDto(
        securityLevelId = SecurityLevelId(UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e51")),
        securityLevelType = SecurityLevelType.FG,
      ),
      SecurityLevelDto(
        securityLevelId = SecurityLevelId(UUID.fromString("8214142f-7c08-48ad-9130-fd7ac6b23e52")),
        securityLevelType = SecurityLevelType.HUSFOLK,
      ),
      SecurityLevelDto(
        securityLevelId = SecurityLevelId(UUID.fromString("8314142f-7c08-48ad-9130-fd7ac6b23e52")),
        securityLevelType = SecurityLevelType.ALLE,
      ),
    )

  fun generateGangData(): List<GangDto> =
    listOf(
      GangDto(gangId = GangId(UUID.fromString("b0bd026f-cc19-4474-989c-aec8d4a76bc9")), name = "Fotogjengen"),
      GangDto(gangId = GangId(UUID.fromString("b1bd026f-cc19-4474-989c-aec8d4a76bc9")), name = "Diversegjengen"),
      GangDto(gangId = GangId(UUID.fromString("b2bd026f-cc19-4474-989c-aec8d4a76bc9")), name = "Regi"),
      GangDto(gangId = GangId(UUID.fromString("b3bd026f-cc19-4474-989c-aec8d4a76bc9")), name = "Musikk"),
      GangDto(gangId = GangId(UUID.fromString("b4bd026f-cc19-4474-989c-aec8d4a76bc9")), name = "Scene"),
      GangDto(gangId = GangId(UUID.fromString("b5bd026f-cc19-4474-989c-aec8d4a76bc9")), name = "Marked"),
    )

  fun generatePlaceData(): List<PlaceDto> =
    listOf(
      PlaceDto(placeId = PlaceId(UUID.fromString("9f4fa5d6-ad7c-419c-be58-1ee73f212675")), name = "Klubben"),
      PlaceDto(placeId = PlaceId(UUID.fromString("8f4fa5d6-ad7c-419c-be58-1ee73f212675")), name = "Storsalen"),
      PlaceDto(placeId = PlaceId(UUID.fromString("7f4fa5d6-ad7c-419c-be58-1ee73f212675")), name = "Bodegaen"),
      PlaceDto(placeId = PlaceId(UUID.fromString("6f4fa5d6-ad7c-419c-be58-1ee73f212675")), name = "Daglighallen"),
      PlaceDto(placeId = PlaceId(UUID.fromString("5f4fa5d6-ad7c-419c-be58-1ee73f212675")), name = "Lyche"),
      PlaceDto(placeId = PlaceId(UUID.fromString("4f4fa5d6-ad7c-419c-be58-1ee73f212675")), name = "Knaus"),
      PlaceDto(placeId = PlaceId(UUID.fromString("3f4fa5d6-ad7c-419c-be58-1ee73f212675")), name = "Strossa"),
      PlaceDto(placeId = PlaceId(UUID.fromString("2f4fa5d6-ad7c-419c-be58-1ee73f212675")), name = "Edgar"),
      PlaceDto(placeId = PlaceId(UUID.fromString("1f4fa5d6-ad7c-419c-be58-1ee73f212675")), name = "Utendørs"),
    )

  fun generateCategoryData(): List<CategoryDto> =
    listOf(
      CategoryDto(categoryId = CategoryId(UUID.fromString("2832ee5e-3f11-4f11-8189-56ca4f70f418")), name = "Gjengfoto"),
      CategoryDto(categoryId = CategoryId(UUID.fromString("3832ee5e-3f11-4f11-8189-56ca4f70f418")), name = "Konsert"),
      CategoryDto(categoryId = CategoryId(UUID.fromString("4832ee5e-3f11-4f11-8189-56ca4f70f418")), name = "Teater"),
      CategoryDto(categoryId = CategoryId(UUID.fromString("5832ee5e-3f11-4f11-8189-56ca4f70f418")), name = "Debatt"),
      CategoryDto(categoryId = CategoryId(UUID.fromString("6832ee5e-3f11-4f11-8189-56ca4f70f418")), name = "Revy"),
      CategoryDto(categoryId = CategoryId(UUID.fromString("7832ee5e-3f11-4f11-8189-56ca4f70f418")), name = "Møte"),
      CategoryDto(categoryId = CategoryId(UUID.fromString("8832ee5e-3f11-4f11-8189-56ca4f70f418")), name = "Fest"),
      CategoryDto(categoryId = CategoryId(UUID.fromString("9832ee5e-3f11-4f11-8189-56ca4f70f418")), name = "Workshop"),
    )

  fun generateEventOwnerData(): List<EventOwnerDto> =
    listOf(
      EventOwnerDto(eventOwnerId = EventOwnerId(UUID.fromString("9265f73d-7b13-4673-9f3b-1db3b6c7d526")), name = EventOwnerName.valueOf("Samfundet")),
      EventOwnerDto(eventOwnerId = EventOwnerId(UUID.fromString("afc308c4-06e2-47bb-b97b-70eb3f55e8d9")), name = EventOwnerName.valueOf("ISFIT")),
      EventOwnerDto(eventOwnerId = EventOwnerId(UUID.fromString("e91f1201-e0bf-4d25-8026-b2a2d44c37c3")), name = EventOwnerName.valueOf("UKA")),
      EventOwnerDto(eventOwnerId = EventOwnerId(UUID.fromString("f91f1201-e0bf-4d25-8026-b2a2d44c37c3")), name = EventOwnerName.valueOf("Abakus")),
      EventOwnerDto(eventOwnerId = EventOwnerId(UUID.fromString("091f1201-e0bf-4d25-8026-b2a2d44c37c3")), name = EventOwnerName.valueOf("Online")),
      EventOwnerDto(eventOwnerId = EventOwnerId(UUID.fromString("191f1201-e0bf-4d25-8026-b2a2d44c37c3")), name = EventOwnerName.valueOf("Tihlde")),
    )

  fun generateAlbumData(): List<AlbumDto> =
    listOf(
      // Recent albums
      AlbumDto(albumId = AlbumId(UUID.fromString("8a2bb663-1260-4c16-933c-a2af7420f5ff")), title = "Vår 2024", isAnalog = false),
      AlbumDto(albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f1")), title = "Høst 2023", isAnalog = false),
      AlbumDto(albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f2")), title = "Vår 2023", isAnalog = false),
      AlbumDto(albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f3")), title = "Høst 2022", isAnalog = false),
      AlbumDto(albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f4")), title = "Vår 2022", isAnalog = false),
      AlbumDto(albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f5")), title = "Høst 2021", isAnalog = false),
      // Historical digital albums
      AlbumDto(albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f6")), title = "Vår 2020", isAnalog = false),
      AlbumDto(albumId = AlbumId(UUID.fromString("91fcac35-4e68-400a-a43e-e8d3f81d10f7")), title = "Høst 2019", isAnalog = false),
      // Analog albums
      AlbumDto(albumId = AlbumId(UUID.fromString("a1fcac35-4e68-400a-a43e-e8d3f81d10f1")), title = "Film Archive 1990s", isAnalog = true),
      AlbumDto(albumId = AlbumId(UUID.fromString("a2fcac35-4e68-400a-a43e-e8d3f81d10f1")), title = "Film Archive 2000s", isAnalog = true),
      AlbumDto(albumId = AlbumId(UUID.fromString("a3fcac35-4e68-400a-a43e-e8d3f81d10f1")), title = "Film Archive 2010s", isAnalog = true),
    )

  fun generatePhotoTagData(): List<PhotoTagDto> =
    listOf(
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8590")), name = "WowFactor100"),
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8591")), name = "insane!"),
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8592")), name = "Meh"),
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8593")), name = "Blurry"),
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8594")), name = "Funny"),
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8595")), name = "Atmospheric"),
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8596")), name = "Candid"),
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8597")), name = "Professional"),
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8598")), name = "Vintage"),
      PhotoTagDto(photoTagId = PhotoTagId(UUID.fromString("d8771ab3-28a9-4b8c-991d-01f6123b8599")), name = "Action"),
    )

  fun generatePositionData(): List<PositionDto> =
    listOf(
      PositionDto(positionId = PositionId(UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f946")), title = "Gjengsjef", email = Email("fg-gjengsjef@samfundet.no")),
      PositionDto(positionId = PositionId(UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f945")), title = "Web", email = Email("fg-web@samfundet.no")),
      PositionDto(positionId = PositionId(UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f944")), title = "Nestsjef", email = Email("fg-nestsjef@samfundet.no")),
      PositionDto(positionId = PositionId(UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f943")), title = "Økonomi", email = Email("fg-okonomi@samfundet.no")),
      PositionDto(positionId = PositionId(UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f942")), title = "Utstyr", email = Email("fg-utstyr@samfundet.no")),
      PositionDto(positionId = PositionId(UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f941")), title = "Arkiv", email = Email("fg-arkiv@samfundet.no")),
      PositionDto(positionId = PositionId(UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f940")), title = "Pang", email = Email("fg-pang@samfundet.no")),
      PositionDto(positionId = PositionId(UUID.fromString("bdd0cf5a-c952-41b8-8b83-c071da51f939")), title = "Gammel", email = Email("fg-gammel@samfundet.no")),
    )

  fun generateSamfundetUserData(): List<SamfundetUserDto> {
    val securityLevels = generateSecurityLevelData()
    return listOf(
      SamfundetUserDto(
        samfundetUserId = SamfundetUserId(UUID.fromString("6a89444f-25f6-44d9-8a73-94587d72b822")),
        email = Email("sindre.sivertsen@samfundet.no"),
        firstName = "Sindre", lastName = "Sivertsen", username = "sjsivert", sex = "Mann",
        profilePicturePath = "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        securityLevel = securityLevels[0], phoneNumber = PhoneNumber("91382506")
      ),
      SamfundetUserDto(
        samfundetUserId = SamfundetUserId(UUID.fromString("7a89444f-25f6-44d9-8a73-94587d72b822")),
        email = Email("carolin.sandbraten@samfundet.no"),
        firstName = "Carolin", lastName = "Sandbråten", username = "carossa", sex = "Kvinne",
        profilePicturePath = "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        securityLevel = securityLevels[0], phoneNumber = PhoneNumber("98765432")
      ),
      SamfundetUserDto(
        samfundetUserId = SamfundetUserId(UUID.fromString("8a89444f-25f6-44d9-8a73-94587d72b822")),
        email = Email("erik.nordstrom@samfundet.no"),
        firstName = "Erik", lastName = "Nordström", username = "erikn", sex = "Mann",
        profilePicturePath = "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        securityLevel = securityLevels[1], phoneNumber = PhoneNumber("87654321")
      ),
      SamfundetUserDto(
        samfundetUserId = SamfundetUserId(UUID.fromString("9a89444f-25f6-44d9-8a73-94587d72b822")),
        email = Email("anna.larsen@samfundet.no"),
        firstName = "Anna", lastName = "Larsen", username = "annal", sex = "Kvinne",
        profilePicturePath = "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        securityLevel = securityLevels[1], phoneNumber = PhoneNumber("76543210")
      ),
      SamfundetUserDto(
        samfundetUserId = SamfundetUserId(UUID.fromString("aa89444f-25f6-44d9-8a73-94587d72b822")),
        email = Email("magnus.hansen@samfundet.no"),
        firstName = "Magnus", lastName = "Hansen", username = "magh", sex = "Mann",
        profilePicturePath = "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        securityLevel = securityLevels[2], phoneNumber = PhoneNumber("65432109")
      ),
      SamfundetUserDto(
        samfundetUserId = SamfundetUserId(UUID.fromString("ba89444f-25f6-44d9-8a73-94587d72b822")),
        email = Email("lise.berg@samfundet.no"),
        firstName = "Lise", lastName = "Berg", username = "liseb", sex = "Kvinne",
        profilePicturePath = "https://media1.tenor.com/images/79f8be09f39791c6462d30c5ce42e3be/tenor.gif?itemid=18386674",
        securityLevel = securityLevels[2], phoneNumber = PhoneNumber("54321098")
      )
    )
  }

  fun generatePhotoGangBangerData(): List<PhotoGangBangerDto> {
    val users = generateSamfundetUserData()
    val positions = generatePositionData()
    val relationshipStatuses = listOf(RelationshipStatus.single, RelationshipStatus.single, RelationshipStatus.single) // Use only valid enum values
    val semesters = listOf("V2024", "H2023", "V2023", "H2022", "V2022", "H2021", "V2021", "H2020")
    val cities = listOf("Trondheim", "Oslo", "Bergen", "Stavanger", "Kristiansand", "Tromsø")
    val zipCodes = listOf("7051", "0349", "5020", "4021", "4630", "9019")

    return users.mapIndexed { index, user ->
      PhotoGangBangerDto(
        photoGangBangerId = PhotoGangBangerId(UUID.fromString("${(6+index)}a89444f-25f6-44d9-8a73-94587d72b839")),
        address = "Testveien ${(index + 1) * 10}",
        city = cities[index % cities.size],
        zipCode = zipCodes[index % zipCodes.size],
        isActive = index < 4, // First 4 are active
        isPang = index >= 6, // Last 2 are pangs
        relationShipStatus = relationshipStatuses[index % relationshipStatuses.size],
        semesterStart = SemesterStart(semesters[index % semesters.size]),
        samfundetUser = user,
        position = positions[index % positions.size]
      )
    }
  }

  fun generateMotiveData(): List<MotiveDto> {
    val albums = generateAlbumData()
    val eventOwners = generateEventOwnerData()
    val categories = generateCategoryData()
    
    return listOf(
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc4bcd")),
        title = "Amber Butts spiller på klubben", albumDto = albums[0], 
        eventOwnerDto = eventOwners[0], categoryDto = categories[1], dateCreated = LocalDate.now().minusDays(5)
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc5bcd")),
        title = "High As a Kite 2024", albumDto = albums[0], 
        eventOwnerDto = eventOwners[0], categoryDto = categories[1], dateCreated = LocalDate.now().minusDays(10)
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc6bcd")),
        title = "Fotogjengen Semesterstart", albumDto = albums[1], 
        eventOwnerDto = eventOwners[0], categoryDto = categories[0], dateCreated = LocalDate.now().minusDays(60)
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc7bcd")),
        title = "ISFIT Opening Ceremony", albumDto = albums[2], 
        eventOwnerDto = eventOwners[1], categoryDto = categories[6], dateCreated = LocalDate.now().minusDays(200)
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc8bcd")),
        title = "UKA Hovedscenen", albumDto = albums[3], 
        eventOwnerDto = eventOwners[2], categoryDto = categories[1], dateCreated = LocalDate.now().minusDays(400)
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8cc9bcd")),
        title = "Teater: Hamlet", albumDto = albums[4], 
        eventOwnerDto = eventOwners[0], categoryDto = categories[2], dateCreated = LocalDate.now().minusDays(600)
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8ccabcd")),
        title = "Samfundetmøte Vinter", albumDto = albums[5], 
        eventOwnerDto = eventOwners[0], categoryDto = categories[5], dateCreated = LocalDate.now().minusDays(800)
      ),
      MotiveDto(
        motiveId = MotiveId(UUID.fromString("94540f3c-77b8-4bc5-acc7-4dd7d8ccbbcd")),
        title = "Vintage Film Collection", albumDto = albums[8], 
        eventOwnerDto = eventOwners[0], categoryDto = categories[8], dateCreated = LocalDate.of(1995, 5, 15)
      )
    )
  }

  fun getPhotoFromApi(): String {
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder().uri(URI.create("https://picsum.photos/1200/800")).build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.headers().allValues("location")[0]
  }

  fun generatePhoto(): List<PhotoDto> {
    val motives = generateMotiveData()
    val places = generatePlaceData()
    val securityLevels = generateSecurityLevelData()
    val gangs = generateGangData()
    val albums = generateAlbumData()
    val categories = generateCategoryData()
    val photoGangBangers = generatePhotoGangBangerData()
    val photoTags = generatePhotoTagData()
    val list = mutableListOf<PhotoDto>()

    for (i in 1..150) { // Increased from 50 to 150 for more diversity
      val random = SecureRandom()
      val byte = random.generateSeed(i)
      val uuid = UUID.nameUUIDFromBytes(byte)
      val url = getPhotoFromApi()
      
      // Vary the data more systematically
      val motive = motives[i % motives.size]
      val place = places[i % places.size]
      val securityLevel = securityLevels[i % securityLevels.size]
      val gang = if (i % 4 == 0) null else gangs[i % gangs.size] // 25% of photos have no gang
      val album = albums[i % albums.size]
      val category = categories[i % categories.size]
      val photographer = photoGangBangers[i % photoGangBangers.size]
      
      // Mix of good and bad pictures
      val isGoodPicture = when (i % 5) {
        0, 1, 2 -> true
        3 -> false
        else -> false // or true, depending on your default
      }

      
      // Vary photo tag count (0-3 tags per photo)
      val tagCount = i % 4
      val selectedTags = if (tagCount == 0) emptyList() else photoTags.shuffled().take(tagCount)
      
      // Some photos don't have all URLs (simulating different processing states)
      val hasAllSizes = i % 3 != 0 // 67% have all sizes
      
      list.add(
        PhotoDto(
          photoId = PhotoId(uuid),
          largeUrl = url, // Always required
          smallUrl = url, // Always provide URL to avoid null issues
          mediumUrl = url, // Always provide URL to avoid null issues
          isGoodPicture = isGoodPicture,
          motive = motive,
          placeDto = place,
          securityLevel = securityLevel,
          gang = gang,
          albumDto = album,
          categoryDto = category,
          photoGangBangerDto = photographer,
          photoTags = selectedTags,
          dateTaken = LocalDate.now().minusDays((i * 3).toLong()), // Spread photos over time
        ),
      )
    }
    return list
  }

  fun seedMockData() {
    println("Starting mock data seeding...")
    
    // Seed in dependency order
    generateSecurityLevelData().forEach { securityLevelRepository.create(it) }
    println("Security levels seeded")
    
    generateSamfundetUserData().forEach { samfundetUserRepository.create(it) }
    println("Samfundet users seeded")
    
    generatePositionData().forEach { positionRepository.create(it) }
    println("Positions seeded")
    
    generatePhotoGangBangerData().forEach { photoGangBangerRepository.create(it) }
    println("Photo gang bangers seeded")
    
    generateAlbumData().forEach { albumRepository.create(it) }
    println("Albums seeded")
    
    generatePhotoTagData().forEach { photoTagRepository.create(it) }
    println("Photo tags seeded")
    
    generateCategoryData().forEach { categoryRepository.create(it) }
    println("Categories seeded")
    
    generateEventOwnerData().forEach { eventOwnerRepository.create(it) }
    println("Event owners seeded")
    
    generateMotiveData().forEach { motiveRepository.create(it) }
    println("Motives seeded")
    
    generatePlaceData().forEach { placeRepository.create(it) }
    println("Places seeded")
    
    generateGangData().forEach { gangRepository.create(it) }
    println("Gangs seeded")
    
    // Batch insert photos for better performance
    database.batchInsert(Photos) {
      generatePhoto().map { photoDto ->
        item {
          set(it.isGoodPicture, photoDto.isGoodPicture)
          set(it.id, photoDto.photoId.id)
          set(it.largeUrl, photoDto.largeUrl ?: "")
          set(it.smallUrl, photoDto.smallUrl ?: "")
          set(it.mediumUrl, photoDto.mediumUrl ?: "")
          set(it.motiveId, photoDto.motive.motiveId.id)
          set(it.securityLevelId, photoDto.securityLevel.securityLevelId.id)
          set(it.gangId, photoDto.gang?.gangId?.id)
          set(it.placeId, photoDto.placeDto.placeId.id)
          set(it.photoGangBangerId, photoDto.photoGangBangerDto.photoGangBangerId.id)
          set(it.albumId, photoDto.albumDto.albumId.id)
          set(it.categoryId, photoDto.categoryDto.categoryId.id)
        }
      }
    }
    println("Photos seeded")
    
    // Initialize blob storage containers
    initializeAzureBlobStorageContainers()
    println("Azure blob storage containers initialized")
    
    // Seed additional relationships
    seedPhotographyRequests()
    seedPurchaseOrders()
    seedArticles()
    seedArticleTags()
    
    println("Mock data seeding completed successfully!")
  }
  
  // Additional seeding methods for other tables
  fun seedPhotographyRequests() {
    // Add some photography requests to test the system
    val requests = listOf(
      mapOf(
        "id" to UUID.randomUUID(),
        "start_time" to LocalDate.now().plusDays(7),
        "end_time" to LocalDate.now().plusDays(7), // Remove plusHours since LocalDate doesn't have it
        "place" to "Storsalen",
        "is_intern" to false,
        "type" to "Konsert",
        "name" to "Ola Nordmann",
        "email" to "ola.nordmann@student.ntnu.no",
        "phone" to "12345678",
        "description" to "Trenger fotografering av konsert med lokale band"
      ),
      mapOf(
        "id" to UUID.randomUUID(),
        "start_time" to LocalDate.now().plusDays(14),
        "end_time" to LocalDate.now().plusDays(14), // Remove plusHours since LocalDate doesn't have it
        "place" to "Klubben",
        "is_intern" to true,
        "type" to "Gjengfoto",
        "name" to "Kari Hansen",
        "email" to "kari.hansen@samfundet.no",
        "phone" to "87654321",
        "description" to "Gjengfoto for ny gjeng som starter opp"
      )
    )
    println("Photography requests would be seeded here")
  }
  
  fun seedPurchaseOrders() {
    // Add some purchase orders with photos
    val orders = listOf(
      mapOf(
        "id" to UUID.randomUUID(),
        "name" to "Sindre Testersen",
        "email" to "sindre@test.no",
        "address" to "Testgata 123",
        "zip_code" to 7051,
        "city" to "Trondheim",
        "send_by_post" to true,
        "comment" to "Vil gjerne ha bilder fra konserten",
        "is_completed" to false
      ),
      mapOf(
        "id" to UUID.randomUUID(),
        "name" to "Anna Eksempel",
        "email" to "anna@eksempel.no", 
        "address" to "Eksempelveien 456",
        "zip_code" to 7030,
        "city" to "Trondheim",
        "send_by_post" to false,
        "comment" to "Digital levering",
        "is_completed" to true
      )
    )
    println("Purchase orders would be seeded here")
  }
  
  fun seedArticles() {
    val photoGangBangers = generatePhotoGangBangerData()
    val securityLevels = generateSecurityLevelData()
    
    val articles = listOf(
      mapOf(
        "id" to UUID.randomUUID(),
        "title" to "Ny fotoworkshop for medlemmer",
        "plain_text" to "Vi arrangerer workshop i grunnleggende fotografi neste uke. Påmelding skjer via nettsiden.",
        "security_level_id" to securityLevels[2].securityLevelId.id, // ALLE
        "photo_gang_banger_id" to photoGangBangers[0].photoGangBangerId.id
      ),
      mapOf(
        "id" to UUID.randomUUID(),
        "title" to "Teknisk oppdatering av kamerautstyr",
        "plain_text" to "Vi har oppgradert vårt kamerautstyr med nye objektiver og belysning.",
        "security_level_id" to securityLevels[0].securityLevelId.id, // FG
        "photo_gang_banger_id" to photoGangBangers[1].photoGangBangerId.id
      ),
      mapOf(
        "id" to UUID.randomUUID(),
        "title" to "Årsrapport fotogjengen 2023",
        "plain_text" to "Se tilbake på et fantastisk år med mange flotte arrangementer og bilder.",
        "security_level_id" to securityLevels[1].securityLevelId.id, // HUSFOLK  
        "photo_gang_banger_id" to photoGangBangers[2].photoGangBangerId.id
      )
    )
    println("Articles would be seeded here")
  }
  
  fun seedArticleTags() {
    val articleTags = listOf(
      mapOf("id" to UUID.randomUUID(), "name" to "Workshop"),
      mapOf("id" to UUID.randomUUID(), "name" to "Utstyr"),
      mapOf("id" to UUID.randomUUID(), "name" to "Årsmøte"),
      mapOf("id" to UUID.randomUUID(), "name" to "Nyhet"),
      mapOf("id" to UUID.randomUUID(), "name" to "Opplæring"),
      mapOf("id" to UUID.randomUUID(), "name" to "Teknisk"),
      mapOf("id" to UUID.randomUUID(), "name" to "Informasjon")
    )
    println("Article tags would be seeded here")
  }
}