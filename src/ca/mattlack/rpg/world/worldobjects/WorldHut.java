package ca.mattlack.rpg.world.worldobjects;

import ca.mattlack.rpg.hitbox.BoundingBox;
import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.render.texture.Textures;
import ca.mattlack.rpg.util.Serializer;
import ca.mattlack.rpg.world.WorldObject;
import ca.mattlack.rpg.world.WorldObjectType;
import ca.mattlack.rpg.world.WorldObjectTypes;

/**
 * A hut world object.
 */
public class WorldHut extends WorldObject {

    @Override
    public WorldObjectType getType() {
        return WorldObjectTypes.HUT;
    }

    public static Serializer serialize(WorldObject tree) {
        return new Serializer();
    }

    public static WorldHut deserialize(Serializer serializer) {
        return new WorldHut();
    }

    @Override
    public IntVector2D getPixelOffset() {
        return new IntVector2D(-82, -82);
    } // Offset of the texture relative to the center of the object.

    @Override
    public Texture getTexture() {
        return Textures.getTexture("hut");
    }

    @Override
    public BoundingBox getBoundingBox() {
        BoundingBox boundingBox = new BoundingBox(new Vector2D(6, 5));
        boundingBox.setPositionModifier(new Vector2D(-2, -3.8)); // Offset of the bounding box relative to the center of the object. (in block units not pixels)
        return boundingBox;
    }
}
