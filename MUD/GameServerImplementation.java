package MUD;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import MUD.Entities.*;

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
        Player player = new Player(playerID, name, "A", Collections.<Item>emptyList());
        _players.add(player);
        _mud.addThing(player.getLocation(), player);
        return player.getID();
    }

    public String getInformation(String clientID) throws RemoteException {
        Player player = getPlayer(clientID);
        return "Server name: " + _name + "\n"
            + "Players online: " + _players.size() + "\n"
            + "To see all available commands type help \n"
            + Terminal.getLine() + "\n"
            + _mud.locationInfo(player.getLocation(), player);
    }

    public String parseInput(String clientID, String clientInput) throws RemoteException {
        Player player = getPlayer(clientID);
        
        switch(Command.evaluate(clientInput)) {
            case HELP:
                return Command.available();
            case SEE:
                return _mud.locationInfo(player.getLocation(), player);
            case MOVE:
                String newLocation = _mud.moveThing(player.getLocation(), Command.getMetadata(), player);
                if (newLocation.equals(player.getLocation())) {
                    return "You tried to move to " + Command.getMetadata() + " however there is no path leading to there";
                } else {
                    player.setLocation(newLocation);
                    return _mud.locationInfo(newLocation, player);
                }
            case PICK:
                Item item = _mud.pick(player.getLocation(), Command.getMetadata());
                if(item != null) {
                    player.items.add(item);
                    return "You successfully picked " + Command.getMetadata() +  "!\n";
                } else {
                    return "Item with name: " + Command.getMetadata() + " doesn't exist";
                }
            case INVENTORY:
                if (player.items.isEmpty()) {
                    return "You don't have any items in your invetory";
                } 

                String items = "Here is your inventory list: \n";
                for(Item _item : player.items) {
                    items += _item.getName() + " ";
                }
                return items;
            case UNKNOWN:
                System.out.println("Command is unknown");
                break;
        }
        return "SUCCESS";
    }


    private Player getPlayer(String id) {
        for(Player player : _players) {
            if(player.getID().equals(id)) {
                return player;
            }
        }
        return null;
    }
}