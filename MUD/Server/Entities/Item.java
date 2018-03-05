package MUD.Server.Entities;

import java.io.Serializable;

public class Item implements Entity, Serializable {
    private String _name;

    public Item(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}