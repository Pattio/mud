package MUD;

public enum Command {
    // Available commands
    UNKNOWN, MOVE;

    // Function to transfer user input to command
    public static Command evaluate(String command) {
        String[] words = command.split("\\s");

        // Commands that consist of 2 words
        if (words.length == 2) {
            return twoWordCommand(words);
        }

        return UNKNOWN;
    }

    public static String getMetadata() {
        return _metadata;
    }

    // Private member variables
    private static String _metadata;

    // Private methods
    private static Command twoWordCommand(String[] words) {
        if (words[0].equals("move") && words[1].matches("north|east|south|west")) {
            _metadata = words[1];
            return MOVE; 
        }

        return UNKNOWN;
    }
}