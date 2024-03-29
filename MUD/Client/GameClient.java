/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Client;

import java.rmi.*;
import java.util.*;

import MUD.Common.*;

public class GameClient {

    // Private members
    private static GameClient client = new GameClient();
    private Scanner scan = new Scanner(System.in);
    private GameServerInterface server;
    private String uniquerPlayerID, serverName;
    private String input, serverResponse, error = "";

    public static void main(String[] args) {
        // Check if enought arguments were passed
        if (args.length < 2) {
            System.err.println("Usage:\njava MUD.Client.GameClient <registry_port> <server_address>");
            return;
        }

        // Gather RMI registry and Server address information
        int port = Integer.parseInt(args[0]);
        String hostname = args[1];

        try {
            // Set security policy
            System.setProperty("java.security.policy", "mud.policy");
            System.setSecurityManager(new SecurityManager());

            // Get server object
            String serverURL = "rmi://" + hostname + ":" + port + "/GameServer";
            System.out.println("Retrieving server info from: " + serverURL);
            client.server = (GameServerInterface) Naming.lookup(serverURL);
            // Start polling server for new events on different thread
            client.startPolling();
            // Create new thread to handle exit
            client.addShutdownHook();
            // Wait for user to either select or create new a server
            while (!client.welcome()) {}
            // Wait for user to login to server
            while(!client.login()) {}
            // Show server information
            client.showServerInformation();

            // Start game loop
            while(true) {
                // Get client input
                client.input = client.scan.nextLine();
                try {
                    // Get server response
                    Terminal.clear();
                    client.serverResponse = client.server.parseInput(client.uniquerPlayerID, client.input);
                    // If server issues quit command, quit the client
                    if (client.serverResponse.equals("QUIT")) {
                        client.uniquerPlayerID = "-1";
                        break;
                    }
                    // Otherwise print server response
                    System.out.println(client.serverResponse);
                } catch(Exception ex) {
                    System.out.println("SERVER IS OFFLINE");
                    break;
                }
            }

        } catch(Exception ex) {
            System.out.println("SERVER IS OFFLINE");
        }
    }

    // Welcome player and ask to either login to an existing server or 
    // create new server
    private boolean welcome() {
        Terminal.clear();
        Terminal.header("WELCOME");
        System.out.println("If you want to login to existing server, write login (servername)");
        System.out.println("If you want to create new server, write create (servername)");
        Terminal.header("SERVERS");
        try {
            // Get and print current servers list
            String serverList = server.getList();
            System.out.println(serverList);
            // Get user input, must be 2 words otherwise return false
            String[] input = client.scan.nextLine().split("\\s");
            if (input.length != 2) {
                System.out.println("You passed wrong number of arguments");
                return false;
            }
            // Check if user wants to login to existing server
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
                // If checks passed save server name
                serverName = input[1];
                return true;
            }
            // Check if user wants to create new server
            if (input[0].equals("create")) {
                // Try to create new server
                if (!server.create(input[1])) return false;
                // If main server return true, save server name
                serverName = input[1];
                return true;
            }

        } catch(Exception ex) {
            System.out.println("SERVER ERROR, TRY AGAIN...");
        }
        return false;
    }

    // Ask user to login to their account
    public boolean login() {
        Terminal.clear();
        // Print error from previous login if it was unsuccessful
        if (error.length() != 0) System.out.println(error);
        Terminal.header("LOGIN");
        System.out.print("Enter username: ");
        String username = scan.nextLine();
        System.out.print("Enter password: ");
        String password = scan.nextLine();
        try {
            // Try to connect user, id must be positive number
            uniquerPlayerID = server.connect(username, password, serverName);
            Terminal.clear();
        } catch(Exception ex) {
            error = "SERVER ERROR, TRY AGAIN...";
            return false;
        }

        // Display errors
        if(uniquerPlayerID.equals("-1")) {
            error = "Server doesn't exists";
            return false;
        }

        if(uniquerPlayerID.equals("-2")) {
            error = "Name and password cannot be empty";
            return false;
        }

        if(uniquerPlayerID.equals("-3")) {
            error = "Someone is already signed into account";
            return false;
        }

        if(uniquerPlayerID.equals("-4")) {
            error = "Incorrect username/password try again";
            return false;
        }

        return true;
    }

    // Get server information
    private void showServerInformation() {
        Terminal.header("SERVER INFORMATION");
        try {
            System.out.println(server.getInformation(client.uniquerPlayerID));
        } catch(Exception ex) {}
    }

    // Create hook which will be run when player exits from terminal
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                // If player has id, inform server about disconnection
                if (uniquerPlayerID == "-1") return;
                try {
                    server.disconnect(uniquerPlayerID);
                    System.out.println("Disconnected from the server");
                } catch(Exception ex) { }
            }
        }));
    }

    // Method to poll server for updates every second
    private void startPolling() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        // If server returns any events print them out to user
                        List<String> events = server.pollEvents(uniquerPlayerID);
                        if (events != null) {
                            for(String event : events) {
                                System.out.println(event);
                            }
                            Terminal.line();
                        }
                        Thread.sleep(1000);
                    } catch(Exception ex) {}
                }
            }
        }).start();
    }
}