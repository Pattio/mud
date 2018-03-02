package MUD;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class GameServerImplementation implements GameServerInterface {
    private MUD _mud = new MUD("Resources/edges", "Resources/messages", "Resources/things");
    private List<Player> _players = new Vector<Player>();
    private String _name;

    public GameServerImplementation(String name) throws RemoteException {
        _name = name;
        Terminal.clear();
        Terminal.header("Game Server initialized");
    }

    public String connect(String name) throws RemoteException {
        System.out.println("Player " + name + " connected to the server");
        String playerID = Integer.toString(_players.size());
        Player player = new Player(playerID, name, "A", Collections.<String>emptyList());
        _players.add(player);
        return player.getID();
    }

    public String getInformation(String clientID) throws RemoteException {
        return "Server name: " + _name + "\n"
            + "Players online: " + _players.size() + "\n"
            + "To see all available commands type help \n"
            + Terminal.getLine();
    }

    public String parseInput(String clientID, String clientInput) throws RemoteException {
        switch(Command.evaluate(clientInput)) {
            case HELP:
                return Command.available();
            case MOVE:
                System.out.println("Client wants to move to " + Command.getMetadata());
                break;
            case UNKNOWN:
                System.out.println("Command is unknown");
                break;
        }
        return "SUCCESS";
    }
}