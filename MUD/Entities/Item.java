package MUD.Entities;

public class Item implements Entity {
    private String _name;

    public Item(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}