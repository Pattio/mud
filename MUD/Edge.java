/***********************************************************************
 * MUD.Edge
 ***********************************************************************/

package MUD;

import java.io.Serializable;

// Represents an path in the MUD (an edge in a graph).
class Edge implements Serializable
{
    public Vertex _dest;   // Your destination if you walk down this path
    public String _view;   // What you see if you look down this path
    
    public Edge( Vertex d, String v )
    {
        _dest = d;
	_view = v;
    }
}

