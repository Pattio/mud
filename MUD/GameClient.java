package MUD;

import java.rmi.*;
import java.util.Scanner;

public class GameClient {

    // Private members
    private Scanner scan = new Scanner(System.in);
    private GameServerInterface server;
    private Player player;

    public static void main(String[] args) {
        GameClient client = new GameClient();

        try {
            // Gather server address information
            String hostname = "macbook";
            int port = 50000;
            // Set security policy
            System.setProperty("java.security.policy", "mud.policy");
            System.setSecurityManager(new SecurityManager());


            String serverURL = "rmi://" + hostname + ":" + port + "/GameServer";
            System.out.println("Retrieving server info from: " + serverURL);
            client.server = (GameServerInterface) Naming.lookup(serverURL);
            client.promptLogin();

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void promptLogin() {
        System.out.println("-----------LOGIN-----------");
        System.out.print("Enter username: ");
        String username = scan.next();
        try {
            player = server.connect(username);
        } catch(Exception ex) {
            System.out.println("Server error");
        }

        System.out.print("\033[H\033[2J");  
        System.out.flush();  
        player.greet();
    }
}