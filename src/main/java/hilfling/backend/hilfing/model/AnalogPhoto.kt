package hilfling.backend.hilfing.model

import javax.persistence.*

@Entity
@Table(name = "analog_photo")
class AnalogPhoto(
        smallUrl: String = "",
        mediumUrl: String = "",
        largeUrl: String = "",
        goodPicture: Boolean = false,
        motive: Motive,
        place: Place,
        securityLevel: SecurityLevel,
        gang: Gang,
        photoGangBanger: PhotoGangBanger,
        @Column(name = "image_number", nullable = false)
        val imageNumber: Int,
        @Column(name = "page_number", nullable = false)
        val pageNumber: Int
): Photo(
        smallUrl,
        mediumUrl,
        largeUrl,
        goodPicture,
        motive,
        place,
        securityLevel,
        gang,
        photoGangBanger
)