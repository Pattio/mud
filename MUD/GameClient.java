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
    private String serverName;

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

            // Wait for user to either select or create new a server
            while (!client.welcome()) {}
            // Wait for user to login to server
            while(!client.login()) {}
            // Show server information
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

    private boolean welcome() {
        Terminal.clear();
        Terminal.header("WELCOME");
        System.out.println("If you want to login to existing server, write login (servername)");
        System.out.println("If you want to create new server, write create (servername)");
        Terminal.header("SERVERS");
        try {
            String serverList = server.getList();
            System.out.println(serverList);

            String[] input = client.scan.nextLine().split("\\s");
            if (input.length != 2) {
                System.out.println("You passed wrong number of arguments");
                return false;
            }

            if (input[0].equals("login")) {
                // Check if server exists
                if (!server.exists(input[1])) {
                    System.out.println("Server does not exists");
                    return false;
                }

                // Check if there is space on the server 
                if (!server.hasSpace(input[1])) {
                    System.out.println("Server is full");
                    return false;
                }

                serverName = input[1];
                return true;
            }

            if (input[0].equals("create")) {
                // try to create new server
                if (!server.create(input[1])) {
                    System.out.println("Could not create new server, max number of servers reached");
                    return false;
                }
                serverName = input[1];
                return true;
            }

        } catch(Exception ex) {
            System.out.println("SERVER ERROR, TRY AGAIN...");
        }
        return false;
    }

    public boolean login() {
        Terminal.clear();
        Terminal.header("LOGIN");
        System.out.print("Enter username: ");
        String username = scan.nextLine();
        try {
            uniquerPlayerID = server.connect(username, serverName);
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