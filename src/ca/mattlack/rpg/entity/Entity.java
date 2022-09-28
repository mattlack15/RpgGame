package ca.mattlack.rpg.entity;

import ca.mattlack.rpg.hitbox.BoundingBox;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.render.GameRenderer;
import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.world.Block;
import ca.mattlack.rpg.world.Location;
import ca.mattlack.rpg.world.World;
import ca.mattlack.rpg.world.WorldObject;

import java.awt.*;
import java.util.UUID;

public class Entity {

    // The world that this entity is in.
    private World world;

    // The position and velocity of this entity.
    private Vector2D position = new Vector2D(0, 0);
    private Vector2D velocity = new Vector2D(0, 0);

    // The hitbox of this entity.
    private BoundingBox boundingBox = new BoundingBox(new Vector2D(0.8, 0.8));

    // The entity id of this entity.
    private UUID id = UUID.randomUUID();

    // The name of this entity.
    private String name = "";

    public Entity(World world) {
        this.setWorld(world);
    }

    public Entity(World world, UUID id) {
        this.setWorld(world);
        this.id = id;
    }

    public World getWorld() {
        return world;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Location getLocation() {
        return new Location(world, position);
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorld(World world) {

        // Store the old world temporarily.
        World oldWorld = this.world;

        // Set the new world.
        this.world = world;

        if (oldWorld != null) {
            // Remove ourselves from the old world.
            oldWorld.entityLeave(this);
        }

        // Add ourselves to the new world.
        world.entityJoin(this);
    }

    public void remove() {
        if (world != null) {
            // Remove ourselves from the world.
            world.entityLeave(this);
        }
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void teleport(Location location) {
        setWorld(location.getWorld());
        setPosition(location.getPosition());
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public void updatePosition(double delta) {
        move(velocity.multiply(delta));
    }

    public void move(Vector2D delta) {

        if (delta.length() == 0) {
            return;
        }

        // Split it into x movement and y movement.
        // We can then check each of the components individually
        // to see if we can move in that direction. This will work
        // because all hit boxes are axis-aligned.
        Vector2D xMovement = new Vector2D(delta.getX(), 0);
        Vector2D yMovement = new Vector2D(0, delta.getY());

        // If we're already colliding with something we don't want to restrict movement and freeze the player.
        // Instead, we want the player to be able to move through the block until they are outside of it.
        boolean alreadyColliding = !tryMove(new Vector2D(0, 0));

        // Try to move in each direction individually and check for a collision.
        // Also, try a few times, dividing by 2 each time to try to get
        // closer to the thing we might be colliding with.
        for (int i = 0; i < 3; i++) {
            if (alreadyColliding || tryMove(xMovement)) {
                position = position.add(xMovement); // Move the entity in the x direction.
                break;
            } else {
                xMovement = xMovement.divide(2);
            }
        }

        for (int i = 0; i < 3; i++) {
            if (alreadyColliding || tryMove(yMovement)) {
                position = position.add(yMovement); // Move the entity in the y direction.
                break;
            } else {
                yMovement = yMovement.divide(2);
            }
        }
    }

    private boolean tryMove(Vector2D delta) {
        // Get the new position.
        Vector2D newPosition = position.add(delta);

        // Look for blocks and world objects around us in a radius of 4.
        for (int x = (int) (newPosition.getX() - 4); x <= (int) (newPosition.getX() + 4); x++) {
            for (int y = (int) (newPosition.getY() - 4); y <= (int) (newPosition.getY() + 4); y++) {

                // Get the block.
                Block block = world.getMap().getBlock(x, y);
                if (block != null) {

                    // If the block is solid, check for a collision.
                    if (block.isSolid()) {
                        // Get the bounding box of the block.
                        BoundingBox blockBoundingBox = block.getBoundingBox();

                        // Check for a collision.
                        if (boundingBox.intersects(
                                newPosition.subtract(boundingBox.getSize().divide(2)), // Subtract half the bounding box's size from the new position so that the bounding box is centered around the new position.
                                blockBoundingBox,
                                new Vector2D(x, y)
                        )) {
                            // We collided so return false.
                            return false;
                        }
                    }
                }

                // Also look for world objects at this position.
                WorldObject worldObject = world.getMap().getWorldObject(x, y);
                if (worldObject != null) {

                    // Get the bounding box of the world object.
                    BoundingBox objectBoundingBox = worldObject.getBoundingBox();

                    // If it exists, check if ours intersects it.
                    if (objectBoundingBox != null) {
                        if (boundingBox.intersects(
                                newPosition.subtract(boundingBox.getSize().divide(2)), // Subtract half the bounding box's size from the new position so that the bounding box is centered around the new position.
                                objectBoundingBox,
                                new Vector2D(x, y)
                        )) {
                            // If we would collide with an object, we can't move.
                            return false;
                        }
                    }
                }
            }
        }

        // We didn't collide with anything, so return true since we can move.
        return true;
    }

    public Texture getTexture() {
        return null;
    }

    public void draw(Graphics2D graphics2D) {

        // Get the texture.
        Texture texture = getTexture();

        // If we have a texture, draw it.
        if (texture != null) {
            graphics2D.drawImage(texture.getImage(),
                    -(int) (GameRenderer.SCALE * 1.6 / 2),
                    -(int) (GameRenderer.SCALE * 1.6 / 2),
                    (int) (GameRenderer.SCALE * 1.6),
                    (int) (GameRenderer.SCALE * 1.6),
                    null);
        } else {

            // There is no texture, so draw a black rectangle.
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(
                    -(int) (GameRenderer.SCALE / 2),
                    -(int) (GameRenderer.SCALE / 2),
                    (int) (GameRenderer.SCALE),
                    (int) (GameRenderer.SCALE)
            );
        }
    }

    // Just here to be overridden by subclasses if needed. It's called every game tick.
    public void tick() {}
}
