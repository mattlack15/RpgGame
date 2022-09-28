package ca.mattlack.rpg.render.renderers;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderer;

import java.awt.*;

/**
 * Pretty much just a wrapper for the draw function on the gui tracker.
 */
public class GuiRenderer extends Renderer {

    public GuiRenderer() {
        super("gui");
    }

    @Override
    public void render(Graphics2D screen, PlayerCamera camera) {
        Client.getInstance().getGuiTracker().draw(screen); // Literally just draw the gui using the gui tracker's method for it.
    }

    @Override
    public int priority() {
        return 99; // Guis are on top of everything.
    }
}
