package ca.mattlack.rpg.ui.blockselector;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.ui.gui.Gui;
import ca.mattlack.rpg.ui.gui.GuiElement;
import ca.mattlack.rpg.world.Block;
import ca.mattlack.rpg.world.Blocks;

import java.awt.*;

public class BlockSelectorGui extends Gui {

    private final Client client;

    public BlockSelectorGui(Client client) {
        super(600, 400);
        this.client = client;

        // Add all the blocks.

        // The size and spacing of each block.
        int elementSize = 50;
        int elementSpacing = 10;

        // The current row and column.
        int column = 0;
        int row = 0;

        Blocks.AIR.getTexture(); // Make sure the Blocks class is loaded. It should be loaded in the constructor of the Client class but just in case.
        for (Block value : Block.REGISTRY.values()) { // For each block in the registry.
            if (column * (elementSize + elementSpacing) + elementSize + 20 > getWidth()) { // If we need to increment to the next row.
                column = 0; // Reset the column.
                row++; // Increment the row.
            }

            // Convert to x and y positions.
            int xPos = column * (elementSize + elementSpacing) + 20;
            int yPos = row * (elementSize + elementSpacing) + 20;

            // Create the element.
            SelectableBlockElement element = new SelectableBlockElement(this, value, xPos, yPos, elementSize, elementSize);

            // Add the element to the gui.
            addElement(element);

            // Increment the column.
            column++;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setBackground(Color.DARK_GRAY.darker().darker()); // Background of dark gray.
        g.clearRect(0, 0, getWidth(), getHeight());
        super.draw(g); // Draw the elements of the gui by calling the parent method.
    }

    public void clearSelection() {
        // Set all block elements to be unselected.
        for (GuiElement element : getElements()) {
            if (element instanceof SelectableBlockElement) {
                ((SelectableBlockElement) element).setSelected(false);
            }
        }
    }
}
