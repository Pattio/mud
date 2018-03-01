package MUD;

import java.rmi.*;

public interface GameServerInterface extends Remote {
    public String sayHello() throws RemoteException;
}