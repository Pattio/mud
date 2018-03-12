/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Server.Entities;

import java.io.Serializable;

// Class to hold item information
public class Item implements Entity, Serializable {
    private String _name;

    public Item(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}