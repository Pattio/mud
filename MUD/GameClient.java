package MUD;

import java.rmi.*;
import java.util.Scanner;

public class GameClient {

    // Private members
    private Scanner scan = new Scanner(System.in);
    private static GameClient client = new GameClient();
    private GameServerInterface server;
    private String uniquerPlayerID;
    private String input;

    public static void main(String[] args) {

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

            client.showServerInformation();

            // Start game loop
            while(true) {
                client.input = client.scan.nextLine();
                try {
                    Terminal.clear();
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
        Terminal.clear();
        Terminal.header("LOGIN");
        System.out.print("Enter username: ");
        String username = scan.nextLine();
        try {
            uniquerPlayerID = server.connect(username);
            Terminal.clear();
        } catch(Exception ex) {
            System.out.println("SERVER ERROR, TRY AGAIN...");
            return false;
        }

        if(uniquerPlayerID == "-1") {
            System.out.println("Incorrect username/password try again");
            return false;
        }
        return true;
    }

    private void showServerInformation() {
        Terminal.header("SERVER INFORMATION");
        try {
            System.out.println(server.getInformation(client.uniquerPlayerID));
        } catch(Exception ex) {}
    }

}