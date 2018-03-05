package MUD;

import MUD.Entities.*;
import java.util.*;
import java.io.*;

public class AccountManager {

    private List<Player> accounts = new Vector<Player>();
    private String accountsURL = "Accounts/accounts.mud";
    
    public AccountManager() {
        load();
    }

    // Save all account to file
    public void save() {
        try {
            FileOutputStream outputFile = new FileOutputStream(accountsURL);
            ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
            outputStream.writeObject(accounts);
            outputStream.close();
            outputFile.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
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

    // Load accounts from file, it is guaranteed that input stream will return
    // list of Player objects, that's why warnings are suppressed
    @SuppressWarnings("unchecked")
    private void load() {
        try {
            FileInputStream inputFile = new FileInputStream(accountsURL);
            ObjectInputStream inputStream = new ObjectInputStream(inputFile);
            accounts = (List<Player>) inputStream.readObject();
            inputStream.close();
            inputFile.close();
        } catch (Exception ex) { }
    }
}