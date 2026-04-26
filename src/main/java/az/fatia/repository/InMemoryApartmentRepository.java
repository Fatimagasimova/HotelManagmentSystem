package az.fatia.repository;

import az.fatia.model.Apartment;

import java.util.ArrayList;
import java.util.List;

public class InMemoryApartmentRepository implements ApartmentRepository {

    private List<Apartment> apartments = new ArrayList<>();


    @Override
    public void save(Apartment apartment) {
        apartments.add(apartment);
    }

    @Override
    public Apartment findById(int id) {
        for(Apartment apartment : apartments){
            if(apartment.getId() == id){
                return apartment;
            }
        }
        return null;
    }

    @Override
    public List<Apartment> findAll() {
        return new ArrayList<>(apartments);
    }

}