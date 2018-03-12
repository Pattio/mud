/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Server.PersistentStorage;

import java.io.*;
import java.util.*;

import MUD.Common.Terminal;
import MUD.Server.Entities.*;
import MUD.Server.World.*;

public class MUDManager {
    private HashMap<String, MUD> muds = new HashMap<String, MUD>();
    private String mudsURL = "Storage/muds";
    private PersistentStorage persistentStorage = new PersistentStorage();
    
    public MUDManager() {
        // Create multiple muds
        muds.put("n", new MUD("Resources/edges", "Resources/messages", "Resources/things"));
        muds.put("Beyond", new MUD("Resources/edges", "Resources/messages", "Resources/things"));
        // If server are already create load them instead
        if (persistentStorage.<HashMap<String, MUD>>load(mudsURL) != null) {
            muds = persistentStorage.<HashMap<String, MUD>>load(mudsURL);
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
        // If server is already created, do not create it again
        if (muds.get(name) != null) 
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
        serverNames += "Running servers: " + muds.size();
        return serverNames;
    }

    // Save all muds to file
    public void save() {
        persistentStorage.<HashMap<String, MUD>>save(mudsURL, muds);
    }
}