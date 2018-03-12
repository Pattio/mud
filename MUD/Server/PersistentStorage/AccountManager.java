/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Server.PersistentStorage;

import java.io.*;
import java.util.*;

import MUD.Server.Entities.*;

public class AccountManager {
    // Private members
    private PersistentStorage persistentStorage = new PersistentStorage();
    private List<Player> accounts = new Vector<Player>();
    private String accountsURL = "Storage/accounts";
    
    public AccountManager() {
        // Load account from file if it exists
        if (persistentStorage.<List<Player>>load(accountsURL) != null) {
            accounts = persistentStorage.<List<Player>>load(accountsURL);
        }
    }

    // Save all account to file
    public void save() {
        persistentStorage.<List<Player>>save(accountsURL, accounts);
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

    // Creates new account and inserts it into account manager storage
    public Player createAccount(String username, String password, String server, String location) {
        Player player = new Player(generateUniqueID(), username, password, server, location, Collections.<Item>emptyList());
        addAccount(player);
        return player;
    }

    // Add account to account manager storage
    private void addAccount(Player player) {
        // Sanity check to prevent duplicates
        for(Player account : accounts) {
            if (account.getID().equals(player.getID())) return;
        }
        // If passed check add account to account list
        accounts.add(player);
    }

    // Generates unique id for each account
    private String generateUniqueID() {
        return Integer.toString(accounts.size());
    }
}