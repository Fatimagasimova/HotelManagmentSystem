package az.fatia.service;

import az.fatia.config.ConfigManager;
import az.fatia.enums.Status;
import az.fatia.model.Apartment;
import az.fatia.repository.ApartmentRepository;

import java.util.List;

public class ApartmentService {
    private ApartmentRepository apartmentRepository;

    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public void save(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    public Apartment findById(int id) {
        return apartmentRepository.findById(id);
    }

    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

    public void reserve(int id, String clientName) {
        if (!ConfigManager.isHotelChangeStatusEnabled()) {
            System.out.println("ERROR: Changing apartment status is disabled in config!");
            return;
        }

        Apartment found = apartmentRepository.findById(id);
        if (found == null) {
            System.out.println("Apartment not found");
            return;
        }
        if (found.getStatus() != Status.AVAILABLE) {
            System.out.println("Apartment is already reserved");
            return;
        }

        found.setStatus(Status.RESERVED);
        found.setClientName(clientName);

        apartmentRepository.save(found);
    }

    public void release(int id) {
        if (!ConfigManager.isHotelChangeStatusEnabled()) {
            System.out.println("ERROR: Changing apartment status is disabled in config!");
            return;
        }

        Apartment found = apartmentRepository.findById(id);
        if (found == null) {
            System.out.println("Apartment not found");
            return;
        }
        if (found.getStatus() != Status.RESERVED) {
            System.out.println("Apartment is already free");
            return;
        }

        found.setClientName(null);
        found.setStatus(Status.AVAILABLE);

        apartmentRepository.save(found);
    }
}