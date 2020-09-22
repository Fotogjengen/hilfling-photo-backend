package hilfling.backend.hilfing.service

import hilfling.backend.hilfing.model.Gang
import hilfling.backend.hilfing.repositories.GangRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GangService  {
    @Autowired
    var repository: GangRepository? = null
    public override fun getRepository(): GangRepository {
        return repository!!
    }
}

// GetAll()
// GetById()
// Create()
// Delete
// Update