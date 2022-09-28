package ca.mattlack.rpg.render.renderers;

import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.render.GameRenderer;
import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderer;
import ca.mattlack.rpg.world.Block;

import java.awt.*;

/**
 * Renders blocks within view onto the screen.
 */
public class BlockRenderer extends Renderer {
    public BlockRenderer() {
        super("block");
    }

    @Override
    public void render(Graphics2D screen, PlayerCamera camera) {

        // Get the width and height of the screen but with blocks as the unit.
        int widthTiles = camera.getViewWidth() / GameRenderer.SCALE;
        int heightTiles = camera.getViewHeight() / GameRenderer.SCALE;

        // Start with the block in the bottom left corner of the screen.
        IntVector2D startBlockPosition = new IntVector2D(-widthTiles / 2, -heightTiles / 2);
        startBlockPosition = startBlockPosition.add(camera.getPlayer().getPosition().toIntVector2D());

        // Loop through all blocks visible on the screen.
        for (int x = -1; x < widthTiles + 1; x++) {
            for (int y = -1; y < heightTiles + 1; y++) {

                // Create a copy of the screen graphics object so we can move it to where the block is.
                Graphics2D blockGraphics = (Graphics2D) screen.create();

                // Move the new graphics object to where the block is.
                camera.transform(blockGraphics, startBlockPosition.add(new IntVector2D(x, y)).toVector2D());

                // Get the block at that position.
                Block block = camera.getPlayer().getWorld().getMap().getBlock(startBlockPosition.add(new IntVector2D(x, y)));
                if (block == null) continue;

                // Draw that block onto the screen using the graphics object we made and moved above.
                block.draw(blockGraphics);
            }
        }
    }

    @Override
    public int priority() {
        return 0; // Pretty much the first thing to be drawn as it's kind of like the background but above the actual background/
    }
}
