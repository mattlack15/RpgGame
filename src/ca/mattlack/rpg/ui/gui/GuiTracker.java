package ca.mattlack.rpg.ui.gui;

import ca.mattlack.rpg.Client;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiTracker
{
    private final Client client;
    private final List<Gui> guis = new ArrayList<>();

    public GuiTracker(Client client)
    {
        this.client = client;
    }

    public void addGui(Gui gui)
    {
        guis.add(gui);
        gui.tracker = this;
    }

    public void removeGui(Gui gui)
    {
        guis.remove(gui);
    }

    public List<Gui> getGuis()
    {
        return new ArrayList<>(guis);
    }

    public void draw(Graphics2D g)
    {
        for (Gui gui : guis)
        {
            if (gui.isVisible())
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.translate(gui.getX(), gui.getY());
                gui.draw(g2);
            }
        }
    }

    public boolean click(int x, int y)
    {
        for (Gui gui : new ArrayList<>(guis))
        {
            if (gui.isVisible())
            {

                int guiX = gui.getX();
                int guiY = gui.getY();
                int guiWidth = gui.getWidth();
                int guiHeight = gui.getHeight();

                if (x >= guiX && x <= guiX + guiWidth && y >= guiY && y <= guiY + guiHeight)
                {
                    gui.click(x - guiX, y - guiY);
                    return true;
                }

            }
        }
        return false;
    }

    public boolean anyVisible()
    {
        for (Gui gui : guis)
        {
            if (gui.isVisible())
            {
                return true;
            }
        }
        return false;
    }

}
