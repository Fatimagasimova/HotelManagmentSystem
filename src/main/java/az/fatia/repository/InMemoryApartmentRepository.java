package az.fatia.repository;

import az.fatia.config.ConfigManager;
import az.fatia.model.Apartment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InMemoryApartmentRepository implements ApartmentRepository {

    private List<Apartment> apartments = new ArrayList<>();

    @Override
    public void save(Apartment apartment) {
        apartments.removeIf(a -> a.getId().equals(apartment.getId()));
        apartments.add(apartment);
        saveToFile();
    }

    @Override
    public Apartment findById(int id) {
        return apartments.stream()
                .filter(apartment -> apartment.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Apartment> findAll() {
        return new ArrayList<>(apartments);
    }

    public void setApartments(List<Apartment> apartments) {
        this.apartments = apartments;
        saveToFile();
    }

    private void saveToFile() {
        String filePath = ConfigManager.getSerializationPath();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(apartments);
        } catch (IOException e) {
            System.err.println("Error while serializing data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        String filePath = ConfigManager.getSerializationPath();
        File file = new File(filePath);

        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.apartments = (List<Apartment>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while restoring data: " + e.getMessage());
        }
    }
}