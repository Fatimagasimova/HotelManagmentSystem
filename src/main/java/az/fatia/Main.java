package az.fatia;

import az.fatia.command.CommandHandler;
import az.fatia.command.CommandParser;
import az.fatia.repository.InMemoryApartmentRepository;
import az.fatia.service.ApartmentService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InMemoryApartmentRepository repository = new InMemoryApartmentRepository();

        repository.loadFromFile();

        ApartmentService service = new ApartmentService(repository);
        CommandHandler handler = new CommandHandler(service);
        CommandParser parser = new CommandParser();

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Hotel Management System ===");
        System.out.println("Commands:");
        System.out.println("  register {id} {price}         - Register new apartment");
        System.out.println("  reserve {id} {clientName}     - Reserve an apartment");
        System.out.println("  release {id}                  - Release reservation");
        System.out.println("  list                          - List all apartments");
        System.out.println("  exit");
        System.out.println("================================");

        while (true) {
            System.out.print("Please enter your command: ");
            String input = scanner.nextLine();

            if (input.trim().isEmpty()) continue;
            if (input.trim().equals("exit")) {
                scanner.close();
                break;
            }
            String[] parts = parser.parse(input);
            handler.handle(parts);
        }
    }
}