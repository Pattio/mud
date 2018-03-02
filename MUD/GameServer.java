package MUD;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;

public class GameServer {
    public static void main(String[] args) {
        
        // Parse arguments
        int registryPort =  50000;
        int serverPort =    50001;
        
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
            GameServerImplementation gameServer = new GameServerImplementation("Cobra");
            GameServerInterface gameServerStub = (GameServerInterface) UnicastRemoteObject.exportObject(gameServer, serverPort);
            // Rebind stub to registry
            registry.rebind("GameServer", gameServerStub);
            System.out.println("Server is successfully running on hostname: " + hostname);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}