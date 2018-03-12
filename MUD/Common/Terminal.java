/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Common;

public class Terminal {
    public static void clear() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public static void line() {
        System.out.println(Terminal.getLine());
    }

    public static String getLine() {
        return "--------------------------------------------------------";
    }

    public static void header(String text) {
        System.out.println(Terminal.getHeader(text));
    }

    public static String getHeader(String text) {
        // Add 30 char padding from left
        String header = String.format("%30s", text).replace(' ', '=');
        // Add 30 char pading from right
        header = String.format("%-" + (60 - text.length()) + "s", header).replace(' ', '=');
        return header;
    }
}