/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Server;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import MUD.Common.*;
import MUD.Server.Entities.*;
import MUD.Server.PersistentStorage.*;
import MUD.Server.World.*;

public class GameServerImplementation implements GameServerInterface {
    // Private member variables
    private List<Player> _players = new Vector<Player>();
    private int _maxPlayers;
    private AccountManager accountManager = new AccountManager();
    private EventManager eventManager = new EventManager();
    private MUDManager mudManager = new MUDManager();

    public GameServerImplementation(int maxPlayers) throws RemoteException {
        // Create new hook responsible for saving accounts before shutdown
        addShutdownHook();
        
        // Initialize limits
        _maxPlayers = maxPlayers;

        // Inform that server is launched
        Terminal.clear();
        Terminal.header("Game Server initialized");
    }

    // Connect player to specific server
    public String connect(String username, String password, String server) throws RemoteException {
        // Check if server exists
        MUD mud = mudManager.getMUD(server);
        if (mud == null) return "-1";
        // Check if username && password is not empty 
        if(username.trim().isEmpty() || password.trim().isEmpty()) return "-2";
        // Check if no one is already logged in into account
        if (alreadyLoggedIn(username, server)) return "-3";
        // Check if account already exists
        Player player = accountManager.getAccount(username, server);
        if (player == null) {
            // If account doesn't exist create it 
            player = accountManager.createAccount(username, password, server, mud.startLocation());
            addPlayer(player, mud);
            return player.getID();
        }
        // If account exists check if password is correct
        if (player.getPassword().equals(password)) {
            addPlayer(player, mud);
            return player.getID();
        }
        // Account exists, however password is incorrect
        return "-4";
    }

    // Disconnect player from the server
    public void disconnect(String clientID) throws RemoteException {
        Player player = getPlayer(clientID);
        removePlayer(player);
    }

    // Get list of all the servers and players count
    public String getList() throws RemoteException {
        return mudManager.getList() + "\nOnline players: " + _players.size() + "/" + _maxPlayers + "\n" + Terminal.getLine();
    }

    // Check if server exists
    public boolean exists(String server) throws RemoteException {
        return (mudManager.getMUD(server) != null);
    }

    // Check if server has space
    public boolean hasSpace(String server) throws RemoteException {
        return _players.size() < _maxPlayers;
    }

    // Try to create new server
    public boolean create(String server) throws RemoteException {
        // If max number of servers is reached return false
        return mudManager.createMUD(server);
    }

    // Get specific server information
    public String getInformation(String clientID) throws RemoteException {
        Player player = getPlayer(clientID);
        return "Server name: " + player.getServerName() + "\n"
            + "Players online: " + mudManager.getMUD(player).playersCount() + "\n"
            + "To see all available commands type help \n"
            + Terminal.getLine() + "\n"
            + mudManager.getMUD(player).locationInfo(player.getLocation(), player);
    }

    // Parse player input
    public String parseInput(String clientID, String clientInput) throws RemoteException {
        Player player = getPlayer(clientID);

        switch(Command.evaluate(clientInput)) {
            case HELP:
                return Command.available();
            case SEE:
                return mudManager.getMUD(player).locationInfo(player.getLocation(), player);
            case MOVE:
                // Find new location
                String newLocation = mudManager.getMUD(player).moveThing(player.getLocation(), Command.getMetadata(), player);
                // If move command return same location, there is no path there
                if (newLocation.equals(player.getLocation())) {
                    return "You tried to move to " + Command.getMetadata() + " however there is no path leading to there\n" + Terminal.getLine();
                } else {
                    // Otherwise update player location
                    player.setLocation(newLocation);
                    // Inform other players staying on new location about the move
                    eventManager.addEvent(clientID, mudManager.getMUD(player).getNeighboursIDs(player), "Player " + player.getName() + " moved to your current location");
                    return mudManager.getMUD(player).locationInfo(newLocation, player);
                }
            case PICK:
                // Try to pick up item
                Item item = mudManager.getMUD(player).pick(player.getLocation(), Command.getMetadata());
                if(item != null) {
                    player.items.add(item);
                    // Inform other players staying on new location about item beign picked
                    eventManager.addEvent(clientID, mudManager.getMUD(player).getNeighboursIDs(player), "Player " + player.getName() + " picked " + item.getName());
                    return "You successfully picked " + Command.getMetadata() +  "!\n" + Terminal.getLine();
                } else {
                    return "Item with name: " + Command.getMetadata() + " doesn't exist\n" + Terminal.getLine();
                }
            case INVENTORY:
                // Check if inventory is not empty
                if (player.items.isEmpty()) {
                    return "You don't have any items in your invetory\n" + Terminal.getLine();
                } 
                // Print all the items from inventory
                String items = "Here is your inventory list: \n";
                for(Item _item : player.items) items += _item.getName() + " ";
                items += "\n" + Terminal.getLine();
                return items;
            case ONLINE:
                return "Players online: " +  mudManager.getMUD(player).playersCount() + "\n" + Terminal.getLine();
            case QUIT:
                removePlayer(player);
                return "QUIT";
            default:
                return "Command is unknown\n" + Terminal.getLine();
        }
    }

    // Return all the new events for specific player
    public List<String> pollEvents(String clientID) throws RemoteException {
        return eventManager.getEvents(clientID);
    }

    // Find player with specific id
    private Player getPlayer(String id) {
        for(Player player : _players) {
            if(player.getID().equals(id)) {
                return player;
            }
        }
        return null;
    }

    // Add player to MUD
    private void addPlayer(Player player, MUD mud) {
        String message = "Player " + player.getName() + " connected to ";
        System.out.println(message + player.getServerName());
        eventManager.addEvent(player.getID(), mudManager.getMUD(player).getPlayerIDs(), message + "the server");
        _players.add(player);
        mud.addThing(player.getLocation(), player);
    }

    // Remove player from MUD
    private void removePlayer(Player player) {
        String message = "Player " + player.getName() + " disconnected from ";
        System.out.println(message + player.getServerName());
        eventManager.addEvent(player.getID(), mudManager.getMUD(player).getPlayerIDs(), message + "the server");
        mudManager.getMUD(player).delThing(player.getLocation(), player);
        _players.remove(player);
    }

    // Removes all players in case of server shutdown
    private void removePlayers() {
        for (Player player : _players) {
            mudManager.getMUD(player).delThing(player.getLocation(), player);
        }
    }

    // Check if someone is already logged into account
    private boolean alreadyLoggedIn(String username, String server) {
        for (Player player : _players) {
            if (player.getName().equals(username) && player.getServerName().equals(server)) return true;
        }
        return false;
    }

    // Save accounts information on shutdown
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                // Remove all the players
                removePlayers();
                // Save all the accounts to persistent storage
                accountManager.save();
                // Save all the muds to persistent storage
                mudManager.save();
            }
        }));
    }
}