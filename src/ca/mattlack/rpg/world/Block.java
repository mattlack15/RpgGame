package ca.mattlack.rpg.world;

import ca.mattlack.rpg.data.Meta;
import ca.mattlack.rpg.hitbox.BoundingBox;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.render.GameRenderer;
import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.render.texture.Textures;
import ca.mattlack.rpg.util.Registry;

import java.awt.*;

/**
 * Represents the type of a single block.
 */
public class Block {

    public static final Registry<String, Block> REGISTRY = new Registry<>(); // The registry of all blocks.

    private final String identifier; // The identifier of the block.
    private final boolean solid; // Whether the block is solid.
    private final String textureId; // The texture id of the block.

    public Block(String identifier, boolean solid, String textureId) {
        this.identifier = identifier;
        this.solid = solid;
        this.textureId = textureId;
    }

    public String getTextureId() {
        return textureId;
    }

    public boolean isSolid() {
        return solid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getId() {
        return REGISTRY.getIdByObject(this); // Get the id of the block.
    }

    public BoundingBox getBoundingBox() {
        return new BoundingBox(new Vector2D(1, 1)); // Most blocks (all) have a bounding box of size 1x1 blocks.
    }

    public Block register() {
        return REGISTRY.register(identifier, this); // Register the block.
    }

    public void draw(Graphics2D graphics2D) {
        Texture texture = Textures.getTexture(textureId); // Get the texture of the block.
        if (texture != null) {
            graphics2D.drawImage(texture.getImage(), 0, 0, GameRenderer.SCALE+1, GameRenderer.SCALE+1, null); // Draw the texture.
        } else {
            // If the texture is null, draw a dark gray square.
            graphics2D.setColor(Color.DARK_GRAY);
            graphics2D.fillRect(0, 0, GameRenderer.SCALE+1, GameRenderer.SCALE+1);
        }
    }

    public static Block get(String identifier) {
        return REGISTRY.get(identifier);
    }

    public Texture getTexture() {
        return Textures.getTexture(textureId);
    }
}
