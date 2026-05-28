package az.fatia.service;

import az.fatia.config.ConfigManager;
import az.fatia.enums.Status;
import az.fatia.model.Apartment;
import az.fatia.repository.ApartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    @Transactional
    public void save(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    public Apartment findById(int id) {
        return apartmentRepository.findById(id);
    }

    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

    @Transactional
    public void reserve(int id, String clientName) {
        if (!ConfigManager.isHotelChangeStatusEnabled()) {
            throw new IllegalStateException("Changing apartment status is disabled in configuration!");
        }

        Apartment found = apartmentRepository.findById(id);
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
        if (!ConfigManager.isHotelChangeStatusEnabled()) {
            throw new IllegalStateException("Changing apartment status is disabled in configuration!");
        }

        Apartment found = apartmentRepository.findById(id);
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