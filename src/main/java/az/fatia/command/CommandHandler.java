package az.fatia.command;

import az.fatia.model.Apartment;
import az.fatia.service.ApartmentService;

import java.math.BigDecimal;

public class CommandHandler {


    private ApartmentService apartmentService;

    public CommandHandler(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    public void handle(String[] parts) {

        if (parts.length < 1) {
            System.out.println("Invalid command");
            return;
        }

        String action = parts[0];

        switch (action) {
            case "register":
                int id = Integer.parseInt(parts[1]);
                BigDecimal price = new BigDecimal(parts[2]);
                Apartment newApartment = new Apartment(id, price);
                apartmentService.save(newApartment);
                System.out.println("New apartment has been saved");
                break;
            case "list":
                System.out.println("Apartments | id | price | status | clientName");
                apartmentService.findAll().forEach(System.out::println);
                break;
            case "reserve":
                int reserveId = Integer.parseInt(parts[1]);
                String clientName = parts[2];
                apartmentService.reserve(reserveId, clientName);
                System.out.println("Reservation has been saved");
                break;
            case "release":
                int releaseId = Integer.parseInt(parts[1]);
                apartmentService.release(releaseId);
                System.out.println("Apartment has been released");
                break;
            default:
                System.out.println("Invalid action");
                break;
        }
    }
}
