/***********************************************************************
 * MUD.Vertex
 ***********************************************************************/

package MUD;

import MUD.Entities.*;
import java.io.Serializable;

import java.util.*;

// Represents a location in the MUD (a vertex in the graph).
class Vertex implements Serializable
{
    public String _name;             // Vertex name
    public String _msg = "";         // Message about this location
    public Map<String,Edge> _routes; // Association between direction
				     // (e.g. "north") and a path
				     // (Edge)
    public List<Entity> _things;     // The things (e.g. players) at
				     // this location

    public Vertex( String nm )
    {
	_name = nm; 
	_routes = new HashMap<String,Edge>(); // Not synchronised
	   _things = new Vector<Entity>();
    }

    public String toString(Entity entity)
    {
	   String summary = _msg + "\n";

	   Iterator iter = _routes.keySet().iterator();
	   String direction;
	   while (iter.hasNext()) {
	       direction = (String)iter.next();
	       summary += "To the " + direction + " there is " + ((Edge)_routes.get( direction ))._view + "\n";
	   }

        if (_things.size() > 1) summary += "\nYou can see: ";
        for(Entity _entity : _things) {
            if(_entity == entity) continue;
            summary += _entity.getName() + " ";
        }

	   summary += "\n" + Terminal.getLine();
	   return summary;
    }

    // Check all the entities at the location and if it's of type Item
    // remove it from location and return casted object
    public Item pickItem(String itemName) {
        for(Entity entity : _things) {
            if(entity instanceof Item && entity.getName().equals(itemName)) {
                _things.remove(entity);
                return (Item) entity;
            }
        }
        return null;
    }
}

