package az.fatia.controller;

import az.fatia.enums.Status;
import az.fatia.model.Apartment;
import az.fatia.repository.ApartmentRepository;
import az.fatia.service.ApartmentService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService service;

    @GetMapping
    public List<Apartment> getAllApartments() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> registerApartment(@RequestBody Apartment newApartment) {
        newApartment.setStatus(Status.AVAILABLE);
        newApartment.setClientName(null);

        service.save(newApartment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Apartment registered successfully"));
    }

    @PutMapping("/{id}/reserve")
    public ResponseEntity<?> reserveApartment(@PathVariable int id, @RequestBody Map<String, String> body) {
        Apartment apartment = service.findById(id);
        if (apartment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Apartment not found"));
        }

        String clientName = body.get("clientName");
        if (clientName == null || clientName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Client name is required for reservation"));
        }

        service.reserve(id, clientName);
        return ResponseEntity.ok(Map.of("message", "Apartment reserved successfully"));
    }

    @PutMapping("/{id}/release")
    public ResponseEntity<?> releaseApartment(@PathVariable int id) {
        Apartment apartment = service.findById(id);
        if (apartment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Apartment not found"));
        }

        service.release(id);
        return ResponseEntity.ok(Map.of("message", "Apartment released successfully"));
    }
}