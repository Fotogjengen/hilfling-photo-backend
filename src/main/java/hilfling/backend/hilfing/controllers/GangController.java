package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.Gang;
import hilfling.backend.hilfing.service.GangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gangs")
public class GangController {
    @Autowired
    private GangService gangService;

    @PostMapping
    public ResponseEntity<Gang> createGang(@Valid @RequestBody Gang gang) {
        return gangService.createGang(gang);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gang> getGang(@PathVariable("id") Long id) {
        return gangService.getGang(id);
    }

    @GetMapping
    public ResponseEntity<List<Gang>> getAllGangs() {
        return gangService.getAllGangs();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gang> updateGang(@Valid @RequestBody Gang gang, @PathVariable("id") Long id) {
        return gangService.updateGang(gang, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGang(@PathVariable("id") Long id) {
        return gangService.deleteGang(id);
    }


}
