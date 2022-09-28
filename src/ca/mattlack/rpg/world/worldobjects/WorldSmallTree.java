package ca.mattlack.rpg.world.worldobjects;

import ca.mattlack.rpg.hitbox.BoundingBox;
import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.render.GameRenderer;
import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.render.texture.Textures;
import ca.mattlack.rpg.util.Serializer;
import ca.mattlack.rpg.world.WorldObject;
import ca.mattlack.rpg.world.WorldObjectType;

/**
 * A small small tree world object.
 */
public class WorldSmallTree extends WorldObject {
    @Override
    public WorldObjectType getType()
    {
        return WorldObjectType.REGISTRY.get("small_tree");
    }

    @Override
    public Texture getTexture()
    {
        return Textures.getTexture("small-tree");
    }

    public static Serializer serialize(WorldObject tree) {
        return new Serializer();
    }

    public static WorldSmallTree deserialize(Serializer serializer)
    {
        return new WorldSmallTree();
    }

    @Override
    public IntVector2D getPixelOffset()
    {
        return new IntVector2D(-62 + GameRenderer.SCALE/2, -62 + GameRenderer.SCALE/2);
    }

    @Override
    public BoundingBox getBoundingBox() {
        BoundingBox boundingBox = new BoundingBox(new Vector2D(1.4, 1.6));
        boundingBox.setPositionModifier(new Vector2D(-0.3, -2)); // Offset of the bounding box relative to the center of the object. (in block units not pixels)
        return boundingBox;
    }
}
