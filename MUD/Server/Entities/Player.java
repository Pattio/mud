package MUD.Server.Entities;

import java.io.Serializable;
import java.util.*;

public class Player implements Entity, Serializable {
    // Public members 
    public List<Item> items = new Vector<Item>();

    public Player(String uniqueID, String name, String password, String server, String location, List<Item> items) {
        _uniqueID = uniqueID;
        _name = name;
        _password = password;
        _currentServer = server;
        _location = location;
        items = items;
    }

    public void greet() {
        System.out.println("Hello, " + _name);
        System.out.println("Your current location is " + _location);
    }

    public String getID() {
        return _uniqueID;
    }

    public void setLocation(String location) {
        _location = location;
    }

    public String getLocation() {
        return _location;
    }

    public String getName() {
        return _name;
    }

    public String getPassword() {
        return _password;
    }

    public String getServerName() {
        return _currentServer;
    }

    // Private members
    private String _uniqueID;
    private String _name;
    private String _password;
    private String _location;
    private String _currentServer;
}