/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Server;

import java.util.*;

public class EventManager {
    // HashMap, Key is user id and value is list of events
    private HashMap<String, List<String>> events = new HashMap<String, List<String>>();

    public void addEvent(String eventInitiator, List<String> eventReceivers, String event) {
        for (String eventReceiver : eventReceivers) {
            // Skip initiator, because initiator already
            // know about the event 
            if (eventReceiver.equals(eventInitiator)) continue;

            // Check if event receiver is already added to HashMap
            // if no, create HashMap entry with empty list 
            if (!events.containsKey(eventReceiver)) {
                events.put(eventReceiver, new Vector<String>());
            }
            events.get(eventReceiver).add(event);
        }
    }

    public List<String> getEvents(String playerID) {
        return events.remove(playerID);
    }
}