package MUD;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import MUD.Entities.*;

public class GameServerImplementation implements GameServerInterface {
    private List<Player> _players = new Vector<Player>();
    private int _maxPlayers;
    private AccountManager accountManager= new AccountManager();
    private MUDManager mudManager;

    public GameServerImplementation(int maxServers, int maxPlayers) throws RemoteException {
        // Create new hook responsible for saving accounts before shutdown
        addShutdownHook();
        
        // Initialize limits
        mudManager = new MUDManager(maxServers);
        _maxPlayers = maxPlayers;

        // Inform that server is launched
        Terminal.clear();
        Terminal.header("Game Server initialized");
    }

    public String connect(String username, String password, String server) throws RemoteException {
        // Check if server exists
        MUD mud = mudManager.getMUD(server);
        if (mud == null) return "-1";
        if (alreadyLoggedIn(username, server)) return "-2";
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
        return "-3";
    }

    public void disconnect(String clientID) throws RemoteException {
        Player player = getPlayer(clientID);
        removePlayer(player);
    }

    public String getList() throws RemoteException {
        return mudManager.getList() + "\nOnline players: " + _players.size() + "\n" + Terminal.getLine();
    }

    public boolean exists(String server) throws RemoteException {
        return (mudManager.getMUD(server) != null);
    }

    public boolean hasSpace(String server) throws RemoteException {
        return _players.size() < _maxPlayers;
    }

    public boolean create(String server) throws RemoteException {
        // If max number of servers is reached return false
        return mudManager.createMUD(server);
    }

    public String getInformation(String clientID) throws RemoteException {
        Player player = getPlayer(clientID);
        return "Server name: " + player.getServerName() + "\n"
            + "Players online: " + mudManager.getMUD(player).playersCount() + "\n"
            + "To see all available commands type help \n"
            + Terminal.getLine() + "\n"
            + mudManager.getMUD(player).locationInfo(player.getLocation(), player);
    }

    public String parseInput(String clientID, String clientInput) throws RemoteException {
        Player player = getPlayer(clientID);

        switch(Command.evaluate(clientInput)) {
            case HELP:
                return Command.available();
            case SEE:
                return mudManager.getMUD(player).locationInfo(player.getLocation(), player);
            case MOVE:
                String newLocation = mudManager.getMUD(player).moveThing(player.getLocation(), Command.getMetadata(), player);
                if (newLocation.equals(player.getLocation())) {
                    return "You tried to move to " + Command.getMetadata() + " however there is no path leading to there";
                } else {
                    player.setLocation(newLocation);
                    return mudManager.getMUD(player).locationInfo(newLocation, player);
                }
            case PICK:
                Item item = mudManager.getMUD(player).pick(player.getLocation(), Command.getMetadata());
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
                for(Item _item : player.items) items += _item.getName() + " ";
                return items;
            case QUIT:
                removePlayer(player);
                return "QUIT";
            default:
                return "Command is unknown";
        }
    }

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
        System.out.println("Player " + player.getName() + " connected to " + player.getServerName());
        _players.add(player);
        mud.addThing(player.getLocation(), player);
    }

    // Remove player from MUD
    private void removePlayer(Player player) {
        System.out.println("Player " + player.getName() + " disconnected from " + player.getServerName());
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
                removePlayers();
                accountManager.save();
                mudManager.save();
            }
        }));
    }
}