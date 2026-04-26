package az.fatia.repository;

import az.fatia.model.Apartment;

import java.util.List;

public interface ApartmentRepository {

    void save(Apartment apartment);

    Apartment findById(int id);

    List<Apartment> findAll();

}
