package ca.mattlack.rpg.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A simple data storage class.
 * EDIT: This class isn't actually used. It was going to be if I implemented chests but time quickly became a constraint.
 */
public class Meta {

    // Map of the identifier of that data to the data itself.
    private final Map<String, Object> objectMap = new HashMap<>();

    /**
     * Sets the value of the specified key to the specified value.
     */
    public void set(String[] key, int keyStart, Object value) {
        // If we are at the end of the key, we can set the value.
        if (keyStart >= key.length - 1) {
            // Set the value.
            objectMap.put(key[keyStart], value);
        } else {
            // If we are not at the end of the key, we need to create a new map or set the value of a map.
            // Get the value of the key.
            Object o = objectMap.get(key[keyStart]);
            // If the value is not a map, we need to create a new map.
            if (!(o instanceof Meta)) {
                // Create a new map.
                o = new Meta();
                // Put the new map into the map.
                objectMap.put(key[keyStart], o);
            }
            // Recursively set the value of the map.
            ((Meta) o).set(key, keyStart + 1, value);
        }
    }

    /**
     * Gets the value of the specified key.
     */
    public void set(String key, Object value) {
        set(key.split("\\."), 0, value); // Split the key into an array of strings that were separated by periods and use that as the key.
    }

    /**
     * Remove the value of the specified key.
     */
    public void remove(String key) {
        set(key, null);
    }

    /**
     * Gets the value of the given key.
     */
    public Object get(String key) {
        return get(key.split("\\."), 0);
    }

    /**
     * Gets the value of the given key.
     */
    public Object get(String[] key, int keyStart) {
        // If we are at the end of the key, we can get the value.
        if (keyStart >= key.length - 1) {
            return objectMap.get(key[keyStart]);
        } else {
            // If we are not at the end of the key, we need to get the value as a map.
            Object o = objectMap.get(key[keyStart]);

            // If the value is a map we can recursively get the value.
            // If the value is not a map, we can't get the value so we return null.
            if (o instanceof Meta) {
                return ((Meta) o).get(key, keyStart + 1);
            }
        }
        // We couldn't get the value so we return null.
        return null;
    }

    /**
     * Gets the keys.
     */
    public Set<String> getKeys() {
        return objectMap.keySet();
    }

    /**
     * Gets the values.
     */
    public Collection<Object> getValues() {
        return objectMap.values();
    }

    /**
     * Checks if the given key exists.
     */
    public boolean has(String[] key, int keyStart) {
        return get(key, keyStart) != null;
    }

    /**
     * Checks if the given key exists.
     */
    public boolean has(String key) {
        return has(key.split("\\."), 0);
    }
}
