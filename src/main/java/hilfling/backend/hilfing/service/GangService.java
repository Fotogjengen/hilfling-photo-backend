package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.Gang;
import hilfling.backend.hilfing.repositories.GangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GangService {
    @Autowired
    GangRepository gangRepository;

    public ResponseEntity<Gang> createGang(Gang gang) {
        try {
            Gang savedGang = gangRepository.save(gang);
            return ResponseEntity.ok().body(savedGang);
        } catch (Exception e) {
            return ResponseEntity.status(304).build();
        }
    }

    public ResponseEntity<Gang> getGang(Long id) {
        return gangRepository.findById(id)
                .map(gang -> ResponseEntity.ok().body(gang))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Gang>> getAllGangs() {
        List<Gang> gangs = gangRepository.findAll();
        return ResponseEntity.ok().body(gangs);
    }

    public ResponseEntity<Gang> updateGang(Gang gangDetails, Long id) {
        return gangRepository.findById(id)
                .map(gang -> {
                    gang.setName(gangDetails.getName());
                    Gang updatedGang = gangRepository.save(gang);
                    return ResponseEntity.ok().body(updatedGang);
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteGang(Long id) {
        return gangRepository.findById(id)
                .map(gang -> {
                    gangRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
