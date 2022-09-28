package ca.mattlack.rpg.ui;

import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.world.Block;
import ca.mattlack.rpg.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MiniMap {
    private Image miniMap;

    private int width;
    private int height;

    public MiniMap(int width, int height) {
        this.width = width;
        this.height = height;

        miniMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public Image getMiniMap() {
        return miniMap;
    }

    public void update(Player player) {

        World world = player.getWorld();

        int mapWidth = world.getMap().getWidth(); // Get the width of the map.
        int mapHeight = world.getMap().getHeight(); // Get the height of the map.

        miniMap = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB); // Create a new image with the width and height of the map.

        Graphics2D g = (Graphics2D) miniMap.getGraphics(); // Get the graphics for the mini map.
        g.scale(1, -1); // Flip the y axis.
        g.translate(0, -mapHeight); // Translate down the y axis.

        g.setColor(Color.BLACK); // Set the color to black.
        g.fillRect(0, 0, mapWidth, mapHeight); // Fill the entire image with black.

        for (int x = 0; x < mapWidth; x++) { // For each x coordinate.
            for (int y = 0; y < mapHeight; y++) { // For each y coordinate.
                Block block = world.getMap().getBlock(new IntVector2D(x, y)); // Get the block at the current x and y coordinate.
                if (block != null && block.getTexture() != null) { // If the block is not null and has a texture.
                    g.setColor(block.getTexture().getAverageColor()); // Set the color to the average color of the texture.
                    g.fillRect(x, y, 1, 1); // Fill a single pixel.
                }
            }
        }

        // Draw a red circle at the player's position.
        g.setColor(Color.RED.darker());
        int size = 8;
        g.fillOval((int) player.getPosition().getX() - size / 2, (int) (player.getPosition().getY() - size / 2), size, size);

        g.dispose();

        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); // This new image will be the scaled version.
        Graphics2D g2 = (Graphics2D) scaled.getGraphics();
        g2.drawImage(miniMap, 0, 0, width, height, null); // Draw the mini map onto the scaled image in a scaled manner.
        g2.dispose();

        miniMap = scaled;
    }
}
