/*************************************************************************
* Distributed Systems and Security Assignment
* ------------------------------------------------------------------------
* Edvinas Byla | 51555015 | CS3524
*************************************************************************/

package MUD.Server.PersistentStorage;

import java.io.*;
import java.util.*;

public class PersistentStorage {
    // Load generic serialized type from file, call this only if know
    // return type of specified file. Warning is supressed because of 
    // asumption that caller knows return type
    @SuppressWarnings("unchecked")
    public <T> T load(String fileURL) {
        try {
            FileInputStream inputFile = new FileInputStream(fileURL);
            ObjectInputStream inputStream = new ObjectInputStream(inputFile);
            T objects = (T) inputStream.readObject();
            inputStream.close();
            inputFile.close();
            return objects;
        } catch (Exception ex) { return null; }
    }

    // Serialize and save passed objects to file
    public <T> void save(String fileURL, T objects) {
        try {
            FileOutputStream outputFile = new FileOutputStream(fileURL);
            ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
            outputStream.writeObject(objects);
            outputStream.close();
            outputFile.close();
        } catch (IOException i) {}
    }
}