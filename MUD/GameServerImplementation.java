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

    public String connect(String name) throws RemoteException {
        System.out.println("Player " + name + " connected to the server");
        String playerID = Integer.toString(_players.size());
        Player player = new Player(playerID, name, "A", Collections.<String>emptyList());
        _players.add(player);
        return player.getID();
    }

    // private void printAvailableCommands() {
    //     System.out.println("--------------------------");
    //     System.out.println("Available command list");
    //     System.out.println("--------------------------\n");
    //     System.out.println("move [ n | e | s | w] - moves player to specified location (n - north, e - east, s - south, w - west");
    // }

    public String parseInput(String clientID, String clientInput) throws RemoteException {
        System.out.println("Client " + clientID + " entered input " + clientInput);
        return "SUCCESS";
    }

    public enum Direction {
        North, East, South, West,
    }

}