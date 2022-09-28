package ca.mattlack.rpg.world.edit;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.ui.gui.Gui;
import ca.mattlack.rpg.world.WorldMap;

import java.awt.*;

/**
 * Literally don't worry about this class it was only used during the making of the maps. It has no function in the final product of the game.
 */
public class GuiEditOptionSelector extends Gui
{
    public GuiEditOptionSelector(WorldEditHandler handler)
    {
        super(250, 300);

        int half = getWidth() / 2;
        int elementWidth = half / 3 * 2;

        EditOption copy = new EditOption(new IntVector2D((half - elementWidth) / 2, 10), new IntVector2D(elementWidth, 50), "Copy", () -> {
            IntVector2D pos1 = handler.getPosition1();
            IntVector2D pos2 = handler.getPosition2();

            if (pos1 == null || pos2 == null) {
                this.remove();
                return;
            }

            IntVector2D dimensions = pos2.subtract(pos1).abs().add(new IntVector2D(1, 1));
            IntVector2D min = IntVector2D.minBoxPoint(pos1, pos2);

            WorldMap clip = Client.getInstance().getPlayer().getWorld().getMap().copy(min, dimensions);
            handler.setClipboard(clip);

            System.out.println("Selection cleared.");
            handler.reset();

            System.out.println("Copied.");

            // Remove the gui.
            this.remove();

        });

        EditOption paste = new EditOption(new IntVector2D((half - elementWidth) / 2 + half, 10), new IntVector2D(elementWidth, 50), "Paste", () -> {
            IntVector2D min = Client.getInstance().getPlayer().getPosition().toIntVector2D();

            WorldMap clip = handler.getClipboard();
            if (clip == null) {
                System.out.println("No selection.");
                this.remove();
                return;
            }

            Client.getInstance().getPlayer().getWorld().getMap().paste(clip, min);

            handler.reset();
            System.out.println("Pasted.");

            // Remove the gui.
            this.remove();
        });

        addElement(copy);
        addElement(paste);
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setColor(Color.DARK_GRAY.darker().darker());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.draw(g);
    }
}
