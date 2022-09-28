package ca.mattlack.rpg.item.gui;

import ca.mattlack.rpg.item.Inventory;
import ca.mattlack.rpg.item.ItemStack;
import ca.mattlack.rpg.ui.gui.Gui;

import java.awt.*;

/**
 * A gui for viewing an inventory that is supplied in the constructor.
 */
public class GuiInventory extends Gui {

    private final Inventory inventory;

    public GuiInventory(Inventory inventory) {

        // Call the parent class' constructor with the width and height.
        super(inventory.getSize() * 40 + 10, 40 + 10);

        // Save the inventory in a field.
        this.inventory = inventory;

        // Add the elements.
        for (int i = 0; i < inventory.getSize(); i++) {
            // Create a gui element at the right x coordinate to maintain spacing between the other elements/
            InventoryGuiElement element = new InventoryGuiElement(this, i, i * 40 + 5, 5, 40, 40);

            // Add the element to this gui.
            addElement(element);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

}
