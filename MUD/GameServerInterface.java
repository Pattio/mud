package MUD;

import java.rmi.*;

public interface GameServerInterface extends Remote {
    public Player connect(String name) throws RemoteException;
}