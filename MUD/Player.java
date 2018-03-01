package MUD;

import java.util.List;
import java.io.Serializable;

public class Player implements Serializable {
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

    // Private members
    private String _uniqueID;
    private String _name;
    private String _location;
    private List<String> _items; 
}