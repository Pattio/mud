/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Common;

import java.rmi.*;
import java.util.List;

public interface GameServerInterface extends Remote {
    // Connect player to specific server
    public String connect(String username, String password, String server) throws RemoteException;
    // Get list of all the servers and players count
    public String getList() throws RemoteException;
    // Get specific server information
    public String getInformation(String clientID) throws RemoteException;
    // Parse player input
    public String parseInput(String clientID, String clientInput) throws RemoteException;


    // Disconnect player from the server
    public void disconnect(String clientID) throws RemoteException;
    

    // Check if server exists
    public boolean exists(String server) throws RemoteException;
    // Check if server has space
    public boolean hasSpace(String server) throws RemoteException;
    // Try to create new server
    public boolean create(String server) throws RemoteException;
    
    
    // Return all the new events for specific player
    public List<String> pollEvents(String clientID) throws RemoteException;
}