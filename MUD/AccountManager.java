package MUD;

import MUD.Entities.*;
import java.util.*;
import java.io.*;

public class AccountManager {
    private PersistentManager persistentManager = new PersistentManager();
    private List<Player> accounts = new Vector<Player>();
    private String accountsURL = "Storage/accounts";
    
    public AccountManager() {
        // Load account from file
        accounts = persistentManager.<List<Player>>load(accountsURL);
    }

    // Save all account to file
    public void save() {
        persistentManager.<List<Player>>save(accountsURL, accounts);
    }

    // Check for user account in specific MUD server
    public Player getAccount(String username, String server) {
        for(Player account : accounts) {
            if (account.getName().equals(username) && account.getServerName().equals(server)) {
                return account;
            }
        }
        return null;
    }

    public Player createAccount(String username, String password, String server, String location) {
        Player player = new Player(generateUniqueID(), username, password, server, location, Collections.<Item>emptyList());
        addAccount(player);
        return player;
    }

    private void addAccount(Player player) {
        // Sanity check to prevent duplicates
        for(Player account : accounts) {
            if (account.getID().equals(player.getID())) return;
        }
        // If passed check add account to account list
        accounts.add(player);
    }

    private String generateUniqueID() {
        return Integer.toString(accounts.size());
    }
}