package MUD;

import java.rmi.*;

public class GameClient {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage:\njava GameClient <host> <port>");
            return;
        }

        try {
            // Gather server address information
            String hostname = args[0];
            int port = Integer.parseInt(args[1]);
            // Set security policy
            System.setProperty("java.security.policy", "mud.policy");
            System.setSecurityManager(new SecurityManager());


            String serverURL = "rmi://" + hostname + ":" + port + "/GameServer";
            System.out.println("Looking up " + serverURL);
            GameServerInterface gameServer = (GameServerInterface) Naming.lookup(serverURL);
            System.out.println(gameServer.sayHello());

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}