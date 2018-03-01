package MUD;

import java.rmi.*;

public interface GameServerInterface extends Remote {
    public String connect(String name) throws RemoteException;
    public String parseInput(String clientID, String clientInput) throws RemoteException;
}