package hilfling.backend.hilfing.model

import javax.persistence.*

@Entity
@Table(name = "photo")
@Inheritance(strategy = InheritanceType.JOINED)
open class Photo (
        @Column(name = "s_url") // Is nullable when analog photo
        open var smallUrl: String = "",
        @Column(name = "m_url") // Is nullable when analog photo
        open var mediumUrl: String = "",
        @Column(name = "l_url") // Is nullable when analog photo
        open var largeUrl: String = "",
        @Column(name = "good_picture", nullable = false)
        open var goodPicture: Boolean = false,
        @ManyToOne
        @JoinColumn(name = "motive_id", referencedColumnName = "id", nullable = false)
        open var motive: Motive,
        @ManyToOne
        @JoinColumn(name = "place_id", referencedColumnName = "id", nullable = false)
        open var place: Place,
        @ManyToOne
        @JoinColumn(name = "security_id", referencedColumnName = "id", nullable = false)
        open var securityLevel: SecurityLevel,
        @ManyToOne
        @JoinColumn(name = "gang_id", referencedColumnName = "id")
        open var gang: Gang,
        @ManyToOne
        @JoinColumn(name = "photo_gang_banger_id", referencedColumnName = "id", nullable = false)
        open var photoGangBanger: PhotoGangBanger
): BaseEntity<Long>()