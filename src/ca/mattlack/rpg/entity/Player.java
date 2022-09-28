package ca.mattlack.rpg.entity;

import ca.mattlack.rpg.item.Inventory;
import ca.mattlack.rpg.item.gui.GuiInventory;
import ca.mattlack.rpg.quest.GameQuest;
import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.render.texture.Textures;
import ca.mattlack.rpg.ui.MiniMap;
import ca.mattlack.rpg.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * Represents the player of the game.
 * Unless I make this multiplayer at some point,
 * (which I probably won't because that's a lot of work)
 * there will only ever be one instance of this class.
 */
public class Player extends LivingEntity {

    // The player's minimap.
    private final MiniMap miniMap;

    // Flag to represent whether the player is moving/facing right or left.
    private boolean facingRight = true;

    // The player's inventory.
    private final Inventory inventory = new Inventory(7);

    // The gui to display the player's inventory.
    private final GuiInventory inventoryGui = new GuiInventory(inventory);

    // The player's current active quest.
    private GameQuest quest;

    public Player(World world, UUID id, String name) {
        super(world, id);
        this.setName(name);

        // Create the player's minimap.
        this.miniMap = new MiniMap(100, 100);

        // Set the inventory gui to be invisible at first.
        // Really this is not needed as it is immediately shown
        // by the hot bar renderer.
        inventoryGui.setVisible(false);
    }

    @Override
    public void tick() {
        super.tick(); // Call the parent's tick function.
        miniMap.update(this); // Update the minimap.
    }

    public GameQuest getQuest()
    {
        return quest;
    }

    public void setQuest(GameQuest quest)
    {
        this.quest = quest;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public GuiInventory getInventoryGui() {
        return inventoryGui;
    }

    @Override
    public Texture getTexture() {

        if (!facingRight) { // If the player is facing left, flip the texture.

            // If we haven't already made a left facing texture, make one.
            if (Textures.getTexture("path-character-left") == null) {

                // Get the right facing texture.
                Texture original = Textures.getTexture("path-character");

                // Create a new buffered image that will be a reflected version.
                BufferedImage image = new BufferedImage(original.getImage().getWidth(null), original.getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics2D = image.createGraphics();

                // Do transformations to flip the image.
                graphics2D.translate(image.getWidth() / 2d, image.getHeight() / 2d);
                graphics2D.scale(-1, 1);
                graphics2D.translate(-image.getWidth() / 2d, -image.getHeight() / 2d);
                graphics2D.drawImage(original.getImage(), 0, 0, null);

                // Create and register the new texture.
                Texture texture = new Texture("path-character-left", image);
                Textures.addTexture("path-character-left", texture);
            }

            // Return the left facing texture.
            return Textures.getTexture("path-character-left");
        }

        // Otherwise, return the right facing texture.
        return Textures.getTexture("path-character");
    }

    public MiniMap getMinMap() {
        return miniMap;
    }

    public void setFacingRight(boolean b) {
        facingRight = b;
    }

    public boolean isFacingRight() {
        return facingRight;
    }
}
