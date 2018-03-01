package MUD;

import java.rmi.*;
import java.util.Scanner;

public class GameClient {

    // Private members
    private Scanner scan = new Scanner(System.in);
    private GameServerInterface server;
    private String uniquerPlayerID;
    private String input;

    public static void main(String[] args) {
        GameClient client = new GameClient();

        try {
            // Gather server address information
            String hostname = "macbook";
            int port = 50000;
            // Set security policy
            System.setProperty("java.security.policy", "mud.policy");
            System.setSecurityManager(new SecurityManager());

            // Get server object
            String serverURL = "rmi://" + hostname + ":" + port + "/GameServer";
            System.out.println("Retrieving server info from: " + serverURL);
            client.server = (GameServerInterface) Naming.lookup(serverURL);

            // Continue while loop till user logins
            while(!client.login()) {}

            // Start game loop
            while(true) {
                client.input = client.scan.nextLine();
                try {
                    System.out.println(client.server.parseInput(client.uniquerPlayerID, client.input));
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean login() {
        System.out.print("Enter username: ");
        String username = scan.nextLine();
        try {
            uniquerPlayerID = server.connect(username);
        } catch(Exception ex) {
            System.out.println("SERVER ERROR, TRY AGAIN...");
            return false;
        }

        if(uniquerPlayerID == "-1") {
            System.out.println("Incorrect username/password try again");
            return false;
        }

        System.out.println("Successfully logged in: ");
        System.out.println("If you want to see all available commands type help");

        //server.displayInfo()
        return true;
    }

}