package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.SecurityLevel;
import hilfling.backend.hilfing.service.SecurityLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/security_levels")
public class SecurityController {
    @Autowired
    SecurityLevelService securityLevelService;

    @PostMapping
    public ResponseEntity<SecurityLevel> createSecurityLevel(@Valid @RequestBody SecurityLevel securityLevel) {
        return securityLevelService.createSecurityLevel(securityLevel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecurityLevel> getSecurityLevel(@PathVariable("id") Long id) {
        return securityLevelService.getSecurityLevel(id);
    }

    @GetMapping
    public ResponseEntity<List<SecurityLevel>> getAllSecurityLevels() {
        return securityLevelService.getAllSecurityLevels();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SecurityLevel> updateSecurityLevel(@Valid @RequestBody SecurityLevel securityLevel,
                                                             @PathVariable("id") Long id) {
        return securityLevelService.updateSecurityLevel(securityLevel, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSecurityLevel(@PathVariable("id") Long id) {
        return securityLevelService.deleteSecurityLevel(id);
    }
}
