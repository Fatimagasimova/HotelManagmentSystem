package az.fatia.controller;

import az.fatia.enums.Status;
import az.fatia.model.Apartment;
import az.fatia.repository.ApartmentRepository;
import az.fatia.service.ApartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    private final ApartmentRepository repository;
    private final ApartmentService apartmentService;

    public ApartmentController(ApartmentRepository repository, ApartmentService apartmentService) {
        this.repository = repository;
        this.apartmentService = apartmentService;
    }

    @GetMapping
    public List<Apartment> getAllApartments() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> registerApartment(@RequestBody Apartment newApartment) {
        newApartment.setStatus(Status.AVAILABLE);
        newApartment.setClientName(null);

        repository.save(newApartment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Apartment registered successfully"));
    }

    @PutMapping("/{id}/reserve")
    public ResponseEntity<?> reserveApartment(@PathVariable int id, @RequestBody Map<String, String> body) {
        Apartment apartment = repository.findById(id);
        if (apartment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Apartment not found"));
        }

        String clientName = body.get("clientName");
        if (clientName == null || clientName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Client name is required for reservation"));
        }

        apartmentService.reserve(id, clientName);
        return ResponseEntity.ok(Map.of("message", "Apartment reserved successfully"));
    }

    @PutMapping("/{id}/release")
    public ResponseEntity<?> releaseApartment(@PathVariable int id) {
        Apartment apartment = repository.findById(id);
        if (apartment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Apartment not found"));
        }

        apartmentService.release(id);
        return ResponseEntity.ok(Map.of("message", "Apartment released successfully"));
    }
}