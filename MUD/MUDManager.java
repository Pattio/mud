package MUD;

import MUD.Entities.*;
import java.util.*;
import java.io.*;

public class MUDManager {
    private HashMap<String, MUD> muds = new HashMap<String, MUD>();
    private String mudsURL = "Storage/muds";
    private int _maxServers;
    
    public MUDManager(int maxServers) {
        _maxServers = maxServers;
        if (new File("path/to/file.txt").isFile()) {
            load();
        } else {
            // Create multiple muds
            muds.put("n", new MUD("Resources/edges", "Resources/messages", "Resources/things"));
            muds.put("Beyond", new MUD("Resources/edges", "Resources/messages", "Resources/things"));
        }
        load();
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
        if (muds.size() >= _maxServers) 
            return false;
        // Otherwise create new server
        muds.put(name, new MUD("Resources/edges", "Resources/messages", "Resources/things"));
        return true;
    }

    public String getList() {
        String serverNames = "";
        for (String serverName : muds.keySet()) {
            serverNames += serverName + "\n";
        }
        return serverNames;
    }

    // Save all muds to file
    public void save() {
        try {
            FileOutputStream outputFile = new FileOutputStream(mudsURL);
            ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
            outputStream.writeObject(muds);
            outputStream.close();
            outputFile.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    // Load muds from file, it is guaranteed that input stream will return
    // hashmap of <String, MUD> objects, that's why warnings are suppressed
    @SuppressWarnings("unchecked")
    private void load() {
        try {
            FileInputStream inputFile = new FileInputStream(mudsURL);
            ObjectInputStream inputStream = new ObjectInputStream(inputFile);
            muds = (HashMap<String, MUD>) inputStream.readObject();
            inputStream.close();
            inputFile.close();
        } catch (Exception ex) { }
    }
}