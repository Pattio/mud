/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Common;

public class Terminal {

    // Clear terminal screen
    public static void clear() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    // Print line
    public static void line() {
        System.out.println(Terminal.getLine());
    }

    // Create line
    public static String getLine() {
        return "--------------------------------------------------------";
    }

    // Print header with some text inside
    public static void header(String text) {
        System.out.println(Terminal.getHeader(text));
    }

    // Create header which has text inside
    public static String getHeader(String text) {
        // Add 30 char padding from left
        String header = String.format("%30s", text).replace(' ', '=');
        // Add 30 char pading from right
        header = String.format("%-" + (60 - text.length()) + "s", header).replace(' ', '=');
        return header;
    }
}