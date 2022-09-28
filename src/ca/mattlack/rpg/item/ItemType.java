package ca.mattlack.rpg.item;

import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.render.texture.Textures;

/**
 * Enum with all of the types of items in the game.
 */
public enum ItemType {

    ROYAL_SWORD("royal-sword"),
    THYME("thyme"),
    MAP_FRAGMENT("map-fragment"),
    WATER_BOTTLE("water-bottle"),
    MAP("map"),
    SWORD_OF_VRAGON("sword-of-vragon");

    private String textureId;

    ItemType(String textureId) {
        this.textureId = textureId;
    }

    public Texture getTexture() {
        return Textures.getTexture(textureId);
    }
}
