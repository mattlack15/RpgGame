package ca.mattlack.rpg.hitbox;

import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.render.GameRenderer;

import java.awt.*;

public class BoundingBox {

    private final Vector2D size;

    // The position modifier is a just a vector that is added to the positions supplied in the intersects
    // and draw functions. It is used to offset this bounding box by some amount in some direction.
    private Vector2D positionModifier = new Vector2D(0, 0);

    public BoundingBox(Vector2D size) {
        this.size = size;
    }

    public Vector2D getSize() {
        return size;
    }

    public Vector2D getPositionModifier() {
        return positionModifier;
    }

    public void setPositionModifier(Vector2D positionModifier) {
        this.positionModifier = positionModifier;
    }

    /**
     * Given the position of this bounding box, and another bounding box and position,
     * check whether or not this bounding box collides with the other bounding box.
     */
    public boolean intersects(Vector2D myPosition, BoundingBox other, Vector2D otherPosition) {

        myPosition = myPosition.add(positionModifier);
        otherPosition = otherPosition.add(other.positionModifier);

        // If the rightmost part of this box is on the left of the leftmost part of the other box, then there cannot be an intersection.
        // Likewise, if the leftmost part of this box is on the right of the rightmost part of the other box, there cannot be an intersection.
        if (myPosition.getX() + size.getX() < otherPosition.getX() || myPosition.getX() > otherPosition.getX() + other.getSize().getX()) {
            return false;
        }

        // If the topmost part of this box is below the bottom of the other box, there cannot be an intersection.
        // Likewise, if the bottom of this box is above the top of the other box, there cannot be an intersection.
        if (myPosition.getY() + size.getY() < otherPosition.getY() || myPosition.getY() > otherPosition.getY() + other.getSize().getY()) {
            return false;
        }

        // There is an intersection.
        return true;
    }


    // Only used for debugging.
    public void draw(Graphics2D graphics2D) {

        Vector2D position = new Vector2D(0, 0);
        position = position.add(positionModifier).multiply(GameRenderer.SCALE);

        graphics2D.setColor(Color.RED);
        graphics2D.drawRect((int) position.getX(), (int) position.getY(), (int) (size.getX() * GameRenderer.SCALE), (int) (size.getY() * GameRenderer.SCALE));

        graphics2D.setColor(Color.BLUE);
        graphics2D.fillOval(-2, -2, 4, 4);

    }
}
