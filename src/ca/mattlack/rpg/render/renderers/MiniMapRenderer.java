package ca.mattlack.rpg.render.renderers;

import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderer;
import ca.mattlack.rpg.ui.MiniMap;

import java.awt.*;

public class MiniMapRenderer extends Renderer {
    public MiniMapRenderer() {
        super("minimap");
    }

    @Override
    public void render(Graphics2D screen, PlayerCamera camera) {

        MiniMap miniMap = camera.getPlayer().getMinMap(); // Get the minimap from the player.

        int width = miniMap.getMiniMap().getWidth(null); // Get the width of the minimap.
        int height = miniMap.getMiniMap().getHeight(null); // Get the height of the minimap.

        Image miniMapImage = miniMap.getMiniMap();

        // Draw in the top right corner, with a border of 5 pixels of black and 10 pixels of padding.
        screen.setColor(Color.BLACK);
        screen.fillRect(camera.getViewWidth() - width - 10 - 10, 10 + 25, width + 10, height + 10); // Draw the minimap background/border.
        screen.drawImage(miniMapImage, camera.getViewWidth() - width - 5 - 10, 5 + 10 + 25, null); // Draw the minimap.

        // Underneath the minimap, draw the player's position.
        screen.setColor(Color.WHITE);
        screen.setFont(new Font("Arial", Font.BOLD, 12));
        screen.drawString("(" + String.format("%.1f", camera.getPlayer().getPosition().getX()) + ", " + String.format("%.1f", camera.getPlayer().getPosition().getY()) + ")", camera.getViewWidth() - width - 5 - 10, 5 + 10 + 25 + height + 10);

    }

    @Override
    public int priority() {
        return 98;
    }
}
