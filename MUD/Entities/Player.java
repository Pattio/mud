package MUD.Entities;

import java.util.List;
import java.io.Serializable;

public class Player implements Entity {
    public Player(String uniqueID, String name, String location, List<String> items) {
        _uniqueID = uniqueID;
        _name = name;
        _location = location;
        _items = items;
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

    // Private members
    private String _uniqueID;
    private String _name;
    private String _location;
    private List<String> _items; 
}