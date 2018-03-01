package MUD;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class GameServerImplementation implements GameServerInterface {
    private MUD _mud = new MUD("Resources/edges", "Resources/messages", "Resources/things");
    private List<Player> _players = new Vector<Player>();

    public GameServerImplementation() throws RemoteException { 
        System.out.println("Game Server initialized");
    }

    public Player connect(String name) throws RemoteException {
        System.out.println("Player " + name + " connected to the server");
        Player player = new Player(_players.size(), name, "A", Collections.<String>emptyList());
        _players.add(player);
        return player;
    }

}