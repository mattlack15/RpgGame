package ca.mattlack.rpg.ui.blockselector;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.render.texture.Texture;
import ca.mattlack.rpg.ui.gui.GuiElement;
import ca.mattlack.rpg.world.Block;

import java.awt.*;

public class SelectableBlockElement extends GuiElement {

    private final BlockSelectorGui parent; // The parent gui.
    private Block block; // The block that this represents.
    private boolean selected = false; // Whether or not this element is selected.

    public SelectableBlockElement(BlockSelectorGui parent, Block block, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.parent = parent;
        this.block = block;
    }

    public BlockSelectorGui getParent() {
        return parent;
    }

    public Block getBlock() {
        return block;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public void draw(Graphics2D g) {
        Texture texture = block.getTexture(); // Get the texture for the block.
        if (texture != null) {
            g.drawImage(texture.getImage(), 0, 0, width, height, null); // Draw the texture.
        }
        if (selected) { // If this element is selected, draw a green border around it.
            g.setColor(Color.GREEN.darker());
            g.setStroke(new BasicStroke(3));
            g.drawRect(0, 0, width, height);
        } else { // If this element is not selected, draw a gray border around it.
            g.setColor(Color.DARK_GRAY);
            g.drawRect(0, 0, width, height);
        }
    }

    @Override
    public void clicked() { // If this element is clicked, select it.
        parent.clearSelection();
        setSelected(true);
        Client.getInstance().setPlacingBlock(block); // Set the placing block to the block that we represent.
    }
}
