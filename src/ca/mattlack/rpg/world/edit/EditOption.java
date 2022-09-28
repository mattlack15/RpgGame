package ca.mattlack.rpg.world.edit;

import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.ui.gui.GuiElement;
import org.w3c.dom.Text;

import java.awt.*;

/**
 * Literally don't worry about this class it was only used during the making of the maps. It has no function in the final product of the game.
 */
public class EditOption extends GuiElement
{

    private final Runnable action;
    private final String name;

    public EditOption(IntVector2D position, IntVector2D size, String name, Runnable action)
    {
        super(position.getX(), position.getY(), size.getX(), size.getY());
        this.name = name;
        this.action = action;
    }

    public Runnable getAction()
    {
        return action;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setColor(Color.DARK_GRAY.brighter());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Text.
        g.setColor(Color.DARK_GRAY.darker());
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(name, getWidth() / 2 - fm.stringWidth(name) / 2, getHeight() / 2 + fm.getHeight() / 4);

        // Border.
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public void clicked()
    {
        action.run();
    }
}
