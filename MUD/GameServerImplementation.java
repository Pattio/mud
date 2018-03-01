package MUD;

import java.rmi.*;
import java.rmi.server.*;

public class GameServerImplementation implements GameServerInterface {
    public GameServerImplementation() throws RemoteException { }

    public String sayHello() {
        return "Hello m8";
    }
}