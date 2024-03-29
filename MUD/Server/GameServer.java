/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Server;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;

import MUD.Common.GameServerInterface;

public class GameServer {
    public static void main(String[] args) {
        // Check if enought arguments were passed
        if (args.length < 2) {
            System.err.println("Usage:\njava MUD.Server.GameServer <registry_port> <server_port>");
            return;
        }

         // Parse arguments
        int registryPort =  Integer.parseInt(args[0]);
        int serverPort =    Integer.parseInt(args[1]);
        
        // Try to create registry in case it is not created yet.
        try {
            LocateRegistry.createRegistry(registryPort);
        } catch(RemoteException ex) {}


        try {
            // Find running registry
            String hostname = (InetAddress.getLocalHost()).getCanonicalHostName();
            Registry registry = LocateRegistry.getRegistry(hostname, registryPort);
            // Set security policy
            System.setProperty("java.security.policy", "mud.policy");
            System.setSecurityManager(new SecurityManager());
            // Generate remote objects
            GameServerImplementation gameServer = new GameServerImplementation(10);
            GameServerInterface gameServerStub = (GameServerInterface) UnicastRemoteObject.exportObject(gameServer, serverPort);
            // Rebind stub to registry
            registry.rebind("GameServer", gameServerStub);
            System.out.println("Server is successfully running on hostname: " + hostname);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}