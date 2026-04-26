package az.fatia.command;

public class CommandParser {

    public String[] parse(String command) {
        return command.trim().split("\\s+");
    }
}
