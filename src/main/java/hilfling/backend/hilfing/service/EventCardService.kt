package hilfling.backend.hilfing.service

import hilfling.backend.hilfing.model.BaseModel
import java.io.Serializable
import java.util.*

class EventCardService (): GenericBaseService<EventCard>{
}

override fun setId(id: kotlin.Long?) {
    TODO("Not yet implemented")
}

class EventCard(var Id: Long): Serializable, BaseModel{
    override fun setId(id: Long?) {
        TODO("Not yet implemented")
    }

    override fun getId(): Long {
        TODO("Not yet implemented")
    }
}