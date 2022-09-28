package ca.mattlack.rpg.render.renderers;

import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.item.gui.GuiInventory;
import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderer;

import java.awt.*;

public class HotbarRenderer extends Renderer {
    public HotbarRenderer() {
        super("hotbar");
    }

    @Override
    public void render(Graphics2D screen, PlayerCamera camera) {
        Player player = camera.getPlayer();

        // Render the inventory part.
        GuiInventory inventoryGui = player.getInventoryGui();
        Graphics2D g = (Graphics2D) screen.create();
        g.translate(inventoryGui.getX(), inventoryGui.getY());
        inventoryGui.draw(g);

        // Render the health part with a red bar.
        g.translate(inventoryGui.getWidth() / 8, -10);
        double maxHealth = player.getMaxHealth();
        double health = player.getHealth();

        int fullHealthBarWidth = inventoryGui.getWidth() / 4 * 3; // The width of the full health bar.
        int healthBarWidth = (int) (health / maxHealth * fullHealthBarWidth); // The width of the current health bar.

        g.setColor(Color.RED.darker().darker().darker().darker());
        g.fillRoundRect(0, 0, fullHealthBarWidth, 5, 5, 5); // Draw the full health bar.

        g.setColor(Color.RED.darker());
        g.fillRoundRect(0, 0, healthBarWidth, 5, 5, 5); // Draw the current health bar.
    }

    @Override
    public int priority() {
        return 99;
    }
}
