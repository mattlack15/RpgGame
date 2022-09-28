package ca.mattlack.rpg.world;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class World {

    private String name; // The name of the world.

    private WorldMap map = new WorldMap(); // The map of the world.

    private Map<UUID, Entity> entities = new HashMap<>(); // The entities in the world.


    public World(String name) {
        this.name = name;
    }

    public World(String name, int width, int height) {
        this(name);
        setMap(new WorldMap(width, height));
    }

    public WorldMap getMap() {
        return map;
    }

    public Map<UUID, Entity> getEntities() {
        return entities;
    }

    public void entityJoin(Entity entity) {
        entities.put(entity.getId(), entity); // Add the entity to the world.
    }

    public void entityLeave(Entity entity) {
        entities.remove(entity.getId()); // Remove the entity from the world.
    }

    public String getName() {
        return name;
    }

    public void tick() {
        for (Entity entity : new ArrayList<>(entities.values())) { // For each entity in the world.
            entity.tick(); // Tick the entity.
        }
    }

    public void updatePositions(double delta) {
        for (Entity entity : entities.values()) { // For each entity in the world.
            entity.updatePosition(delta); // Update the position of the entity. Delta is a modifier for the movement to make movement more smooth between frames.
        }
    }

    public void setMap(WorldMap map) {
        this.map = map;
    }

    public World register() {
        Client.getInstance().addWorld(this); // Add the world to the client.
        return this;
    }
}
