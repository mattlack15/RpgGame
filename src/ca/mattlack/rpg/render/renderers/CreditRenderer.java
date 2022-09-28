package ca.mattlack.rpg.render.renderers;

import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderer;

import java.awt.*;

public class CreditRenderer extends Renderer {

    public CreditRenderer() {
        super("credits");
    }

    @Override
    public void render(Graphics2D screen, PlayerCamera camera) {
        // Developer - Matthew Lack
        // Artist - Mariana Cuan Celis

        screen.setColor(Color.WHITE);
        screen.setFont(new Font("Arial", Font.PLAIN, 20));
        screen.drawString("Developer - Matthew Lack", 10, camera.getViewHeight() - 50);
        screen.drawString("Artist - Mariana Cuan Celis", 10, camera.getViewHeight() - 25);
    }

    @Override
    public int priority() {
        return 919;
    }
}
