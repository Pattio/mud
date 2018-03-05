package MUD;

import MUD.Entities.*;
import java.util.*;
import java.io.*;

public class MUDManager {
    private HashMap<String, MUD> muds = new HashMap<String, MUD>();
    private String mudsURL = "Storage/muds";
    private PersistentManager persistentManager = new PersistentManager();
    private int _maxServers;
    
    public MUDManager(int maxServers) {
        _maxServers = maxServers;
        // Create multiple muds
        muds.put("n", new MUD("Resources/edges", "Resources/messages", "Resources/things"));
        muds.put("Beyond", new MUD("Resources/edges", "Resources/messages", "Resources/things"));
        // If server are already create load them instead
        if (persistentManager.<HashMap<String, MUD>>load(mudsURL) != null) {
            muds = persistentManager.<HashMap<String, MUD>>load(mudsURL);
        }
    }

    // Return MUD matching name
    public MUD getMUD(String name) {
        return muds.get(name);
    }

    // Return MUD for player
    public MUD getMUD(Player player) {
        return muds.get(player.getServerName());
    }

    public boolean createMUD(String name) {
        // Checks if it's possible to create more servers 
        // also if server is already created, do not create it again
        if (muds.size() >= _maxServers || muds.get(name) != null) 
            return false;
        // Otherwise create new server
        muds.put(name, new MUD("Resources/edges", "Resources/messages", "Resources/things"));
        return true;
    }

    public String getList() {
        String serverNames = "";
        for (Map.Entry<String, MUD> entry  : muds.entrySet()) {
            serverNames += entry.getKey() + "\t\t\t" + entry.getValue().playersCount() + " players \n";
        }
        serverNames += Terminal.getHeader("STATS") + "\n";
        serverNames += "Running servers: " + muds.size() + "/" + _maxServers;
        return serverNames;
    }

    // Save all muds to file
    public void save() {
        persistentManager.<HashMap<String, MUD>>save(mudsURL, muds);
    }
}