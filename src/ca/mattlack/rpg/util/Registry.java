package ca.mattlack.rpg.util;

import java.util.*;

public class Registry<I, T> {

    private int idCounter = 0;

    private final Map<I, T> identifierToObject = new HashMap<>(); // Maps identifiers to objects.
    private final Map<T, I> objectToIdentifier = new HashMap<>(); // Maps objects to identifiers.
    private final Map<Integer, I> idToIdentifier = new HashMap<>(); // Maps ids to identifiers.
    private final Map<I, Integer> identifierToId = new HashMap<>(); // Maps identifiers to ids.

    public T register(I identifier, T object) {
        if (identifierToObject.containsKey(identifier)) {
            throw new IllegalArgumentException("Identifier already registered: " + identifier);
        }
        if (objectToIdentifier.containsKey(object)) {
            throw new IllegalArgumentException("Object already registered: " + object);
        }

        // Map everything to everything else.
        identifierToObject.put(identifier, object);
        objectToIdentifier.put(object, identifier);
        idToIdentifier.put(idCounter, identifier);
        identifierToId.put(identifier, idCounter);

        // Increment the id counter.
        idCounter++;

        return object;
    }

    public T get(I identifier) {
        return identifierToObject.get(identifier);
    }

    public I getIdentifier(T object) {
        return objectToIdentifier.get(object);
    }

    public int getId(I identifier) {
        if (!identifierToId.containsKey(identifier)) {
            throw new IllegalArgumentException("Identifier not registered: " + identifier);
        }
        return identifierToId.get(identifier);
    }

    public int getIdByObject(T object) {
        return getId(getIdentifier(object));
    }

    public I getIdentifier(int id) {
        return idToIdentifier.get(id);
    }

    public T get(int id) {
        return identifierToObject.get(getIdentifier(id));
    }

    public void clear() {
        // Clear all maps.
        identifierToObject.clear();
        objectToIdentifier.clear();
        idToIdentifier.clear();
        identifierToId.clear();

        // Reset the id counter.
        idCounter = 0;
    }

    public int size() {
        return identifierToObject.size();
    }

    public Set<I> getKeys() {
        return identifierToObject.keySet();
    }

    public Collection<T> values() {
        return identifierToObject.values();
    }
}
