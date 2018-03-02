package MUD;

public enum Command {
    // Available commands
    UNKNOWN, MOVE, HELP;

    // Function to transfer user input to command
    public static Command evaluate(String command) {
        String[] words = command.split("\\s");

        if (words.length == 1) {
            return oneWordCommands(words);
        }

        if (words.length == 2) {
            return twoWordCommand(words);
        }

        return UNKNOWN;
    }

    public static String getMetadata() {
        return _metadata;
    }

    public static String available() {
        return Terminal.getHeader("COMMANDS") 
            + "\nhelp \n"
            + "\tPrints all available commands\n"
            + Terminal.getLine()
            + "\nmove [ north | east | south | west]\n"
            + "\tMoves player to specified location\n"
            + Terminal.getLine();
    }

    // Private member variables
    private static String _metadata;

    // Private methods
    private static Command oneWordCommands(String[] words) {
        if (words[0].equals("help")) {
            return HELP;
        }
        return UNKNOWN;
    }

    private static Command twoWordCommand(String[] words) {
        if (words[0].equals("move") && words[1].matches("north|east|south|west")) {
            _metadata = words[1];
            return MOVE; 
        }

        return UNKNOWN;
    }
}