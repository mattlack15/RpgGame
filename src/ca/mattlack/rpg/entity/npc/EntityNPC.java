package ca.mattlack.rpg.entity.npc;

import ca.mattlack.rpg.entity.Entity;
import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.render.texture.Textures;
import ca.mattlack.rpg.world.World;

/**
 * This entity class is used as an NPC in the game that has a dialog system and an npc texture.
 */
public class EntityNPC extends Entity {

    private Dialog dialog = new Dialog();

    private String textureId = null;

    public EntityNPC(World world) {
        super(world);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog)
    {
        this.dialog = dialog;
    }

    public EntityNPC setTextureId(String textureId) {
        this.textureId = textureId;
        return this;
    }

    // Override the getTexture() function because we want to use a different texture for each NPC.
    @Override
    public Texture getTexture() {
        if (textureId == null) {
            return null;
        }
        return Textures.getTexture(textureId);
    }
}
