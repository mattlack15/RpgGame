package ca.mattlack.rpg.world;

import ca.mattlack.rpg.math.Vector2D;

public class Location {

    private final Vector2D position; // The position of the location in the world.
    private final World world; // The world the location is in.

    public Location(World world, Vector2D position) {
        this.position = position;
        this.world = world;
    }

    public Vector2D getPosition() {
        return position;
    }

    public World getWorld() {
        return world;
    }
}
