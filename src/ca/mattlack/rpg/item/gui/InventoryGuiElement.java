package ca.mattlack.rpg.item.gui;

import ca.mattlack.rpg.item.ItemStack;
import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.ui.gui.GuiElement;

import java.awt.*;

/**
 * Represents an element in a gui. Holds the draw function for an inventory gui element/
 */
public class InventoryGuiElement extends GuiElement {

    private final GuiInventory parent;
    private final int slot;

    /**
     *
     * @param parent The parent inventory gui.
     * @param slot The slot in the inventory that this element represents.
     * @param x The x coordinate that the element should be displayed at relative to the gui origin.
     * @param y The y coordinate that the element should be displayed at relative to the gui origin.
     * @param width The width of the element.
     * @param height The height of the element.
     */
    public InventoryGuiElement(GuiInventory parent, int slot, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.parent = parent;
        this.slot = slot;
    }

    @Override
    public void draw(Graphics2D g) {
        ItemStack itemStack = parent.getInventory().getItem(slot);

        // Draw background.
        g.setColor(new Color(0x66ffffe0, true));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw border.
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(2));
        g.drawRect(0, 0, width, height);

        // Draw item?
        if (itemStack != null) {

            Texture texture = itemStack.getItemType().getTexture();

            if (texture != null) {

                // Flip the image vertically.
                g.translate(0, height);
                g.scale(1, -1);

                g.drawImage(texture.getImage(), 2, 2, width - 4, height - 4, null);

                // Flip the image back and draw the quantity in the bottom right.
                g.scale(1, -1);
                g.translate(0, -texture.getImage().getHeight(null));

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 12));
                g.drawString(String.valueOf(itemStack.getQuantity()), width - g.getFontMetrics().stringWidth(String.valueOf(itemStack.getQuantity())) - 5, height - 5);
            } else {
                // Draw red circle.
                g.setColor(Color.RED);
                g.fillOval(0, 0, width, height);
            }

        }

    }
}
