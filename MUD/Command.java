package MUD;

public enum Command {
    // Available commands
    UNKNOWN, MOVE, HELP, SEE, PICK, INVENTORY, QUIT;

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
            + Terminal.getLine()
            + "\n see\n"
            + "\tShow everything around you (items, players, paths)\n"
            + Terminal.getLine()
            + "\n pick (item-name)\n"
            + "\tPick item with specified name if it exists at current location\n"
            + Terminal.getLine()
            + "\n inventory\n"
            + "\tDisplays items in your inventory\n"
            + Terminal.getLine()
            + "\n quit\n"
            + "\tQuit game\n"
            + Terminal.getLine();
    }

    // Private member variables
    private static String _metadata;

    // Private methods
    private static Command oneWordCommands(String[] words) {
        if (words[0].equals("help")) return HELP;
        if (words[0].equals("see")) return SEE;
        if (words[0].equals("inventory")) return INVENTORY;
        if (words[0].equals("quit")) return QUIT;
        return UNKNOWN;
    }

    private static Command twoWordCommand(String[] words) {
        if (words[0].equals("move") && words[1].matches("north|east|south|west")) {
            _metadata = words[1];
            return MOVE; 
        }

        if (words[0].equals("pick")) {
            _metadata = words[1];
            return PICK;
        } 

        return UNKNOWN;
    }
}