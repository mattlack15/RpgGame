package ca.mattlack.rpg.entity;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.item.ItemStack;
import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.world.World;

import java.awt.*;

// This class is used as an entity in the game to represent an item on the ground.
public class EntityItem extends Entity {

    // This variable stores the ticks that have passed since this entity was created.
    // It is used to help make the item oscillate up and down over time.
    private long tick = 0;

    // The item stack that this entity is representing.
    private ItemStack itemStack;

    public EntityItem(World world, ItemStack itemStack) {
        super(world); // Call the parent's constructor.

        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Texture getTexture() {
        if (itemStack == null) {
            return null;
        }
        return itemStack.getItemType().getTexture();
    }

    @Override
    public void tick() {
        super.tick(); // Call the parent's tick function.
        tick++; // Increment the tick variable.

        // Get the player.
        Player player = Client.getInstance().getPlayer();

        // If the player is close enough, pick up the item.
        // Do this by adding the item we represent to the player's inventory
        // and then removing ourselves from the world.
        if (player.getPosition().distanceSquared(this.getPosition()) < 1.6 && player.getWorld() == this.getWorld()) {
            player.getInventory().addItem(itemStack);
            this.remove();
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        Texture texture = getTexture();
        if (texture == null) {
            return;
        }

        int yOffset = (int) (Math.sin(tick / 5d) * 8);

        graphics2D.drawImage(texture.getImage(),
                -12,
                -12 + yOffset,
                24,
                24,
                null);

        // Add a small oval shadow. The opacity of the shadow is affected by the y offset.
        graphics2D.setColor(new Color(0, 0, 0, 100 - (yOffset * 4)));
        graphics2D.fillOval(-5, -12 - 16, 8, 8);

    }
}
