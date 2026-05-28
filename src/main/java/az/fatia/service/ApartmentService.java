package az.fatia.service;

import az.fatia.enums.Status;
import az.fatia.model.Apartment;
import az.fatia.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;


    @Transactional
    public void save(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    public Apartment findById(int id) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apartment with ID " + id + " not found"));
        return apartment;
    }

    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

    @Transactional
    public void reserve(int id, String clientName) {
        Apartment found = findById(id);
        if (found == null) {
            throw new IllegalArgumentException("Apartment with ID " + id + " not found");
        }

        if (found.getStatus() != Status.AVAILABLE) {
            throw new IllegalStateException("Apartment is already reserved or unavailable");
        }

        found.setStatus(Status.RESERVED);
        found.setClientName(clientName);

        apartmentRepository.save(found);
    }

    @Transactional
    public void release(int id) {
        Apartment found = findById(id);
        if (found == null) {
            throw new IllegalArgumentException("Apartment with ID " + id + " not found");
        }

        if (found.getStatus() != Status.RESERVED) {
            throw new IllegalStateException("Apartment is already free and available");
        }

        found.setClientName(null);
        found.setStatus(Status.AVAILABLE);

        apartmentRepository.save(found);
    }
}