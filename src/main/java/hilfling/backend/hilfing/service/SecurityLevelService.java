package hilfling.backend.hilfing.service;

import hilfling.backend.hilfing.model.SecurityLevel;
import hilfling.backend.hilfing.repositories.SecurityLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityLevelService {
    @Autowired
    SecurityLevelRepository securityLevelRepository;

    public ResponseEntity<SecurityLevel> createSecurityLevel(SecurityLevel securityLevel) {
        try {
            SecurityLevel newSecurityLevel = securityLevelRepository.save(securityLevel);
            return ResponseEntity.ok().body(newSecurityLevel);
        } catch (Exception e) {
            return ResponseEntity.status(304).build();
        }
    }

    public ResponseEntity<SecurityLevel> getSecurityLevel(Long id) {
        return securityLevelRepository.findById(id)
                .map(securityLevel -> ResponseEntity.ok().body(securityLevel))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<SecurityLevel>> getAllSecurityLevels() {
        List<SecurityLevel> securityLevels = securityLevelRepository.findAll();
        return ResponseEntity.ok().body(securityLevels);
    }

    public ResponseEntity<SecurityLevel> updateSecurityLevel(SecurityLevel securityLevelDetails, Long id) {
        return securityLevelRepository.findById(id)
                .map(security -> {
                    security.setType(securityLevelDetails.getType());
                    SecurityLevel updatedSecurityLevel = securityLevelRepository.save(security);
                    return ResponseEntity.ok().body(updatedSecurityLevel);
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteSecurityLevel(Long id) {
        return securityLevelRepository.findById(id)
                .map(securityLevel -> {
                    securityLevelRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
