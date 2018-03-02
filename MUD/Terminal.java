package MUD;

public class Terminal {
    static void clear() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    static void line() {
        System.out.println(Terminal.getLine());
    }

    static String getLine() {
        return "--------------------------------------------------------";
    }

    static void header(String text) {
        System.out.println(Terminal.getHeader(text));
    }

    static String getHeader(String text) {
        // Add 30 char padding from left
        String header = String.format("%30s", text).replace(' ', '=');
        // Add 30 char pading from right
        header = String.format("%-" + (60 - text.length()) + "s", header).replace(' ', '=');
        return header;
    }
}