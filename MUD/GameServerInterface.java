package MUD;

import java.rmi.*;

public interface GameServerInterface extends Remote {
    public String getList() throws RemoteException;
    public boolean exists(String server) throws RemoteException;
    public boolean hasSpace(String server) throws RemoteException;
    public boolean create(String server) throws RemoteException;
    public String connect(String username, String password, String server) throws RemoteException;
    public void disconnect(String clientID) throws RemoteException;
    public String parseInput(String clientID, String clientInput) throws RemoteException;
    public String getInformation(String clientID) throws RemoteException;
}