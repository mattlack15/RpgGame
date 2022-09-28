package ca.mattlack.rpg.render.renderers;

import ca.mattlack.rpg.entity.Entity;
import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderer;
import ca.mattlack.rpg.world.World;

import java.awt.*;

public class EntityRenderer extends Renderer {
    public EntityRenderer() {
        super("entity");
    }

    @Override
    public void render(Graphics2D screen, PlayerCamera camera) {

        Player player = camera.getPlayer();
        World world = player.getWorld();

        // Loop through all entities in the world.
        for (Entity value : world.getEntities().values()) {
            Graphics2D entityGraphics = (Graphics2D) screen.create(); // Create a new graphics object to draw the entity.

            camera.transform(entityGraphics, value.getPosition()); // Move the graphics object to the entity's position.

            value.draw(entityGraphics); // Draw the entity onto the screen.
        }
    }

    @Override
    public int priority() {
        return 1;
    }
}
