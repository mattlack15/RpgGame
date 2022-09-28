package ca.mattlack.rpg.world;

import ca.mattlack.rpg.hitbox.BoundingBox;
import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.render.texture.Texture;

import java.awt.*;

/**
 * This class represents an object in the world that isn't a block and isn't an entity.
 */
public abstract class WorldObject
{

    private IntVector2D position = new IntVector2D(0, 0); // The position of the object in the world.

    public void setPosition(IntVector2D position)
    {
        this.position = position;
    }

    public IntVector2D getPosition()
    {
        return position;
    }

    public abstract WorldObjectType getType(); // Get the type of this object.
    public abstract Texture getTexture(); // Get the texture of this object.
    public IntVector2D getPixelOffset() {
        return new IntVector2D(0, 0);
    }

    public void draw(Graphics2D graphics2D) {

        IntVector2D pixelOffset = getPixelOffset(); // Offset of the texture relative to the center of the object.
        graphics2D.translate(pixelOffset.getX(), pixelOffset.getY());

        Texture texture = getTexture(); // Get the texture of this object.
        Image image = texture.getImage(); // Get the image of the texture.
        graphics2D.drawImage(image, 0, 0, null); // Draw the image.

    }

    // To be overridden by subclasses.
    public BoundingBox getBoundingBox() {
        return null;
    } // Get the bounding box of this object, if any.
}
