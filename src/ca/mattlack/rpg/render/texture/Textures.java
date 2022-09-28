package ca.mattlack.rpg.render.texture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Textures {

    // Map of all textures. Key is the id of the texture, value is the texture.
    private static final Map<String, Texture> TEXTURES = new HashMap<>();

    public static void loadTextures(File folder) {

        File[] files = folder.listFiles();
        if (files == null) return;

        // Go through all files in the folder.
        for (File file : files) {

            // If it's a file.
            if (file.isFile()) {

                String name = file.getName();
                Image image = new ImageIcon(file.getAbsolutePath()).getImage();

                // Get rid of the file extension.
                if (name.split("\\.").length > 1) {
                    name = name.split("\\.")[0];
                }

                // All textures must be flipped vertically.
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics2D = bufferedImage.createGraphics();

                // Flip from the center of the image.
                graphics2D.translate(0, bufferedImage.getHeight() / 2d);
                graphics2D.scale(1, -1);
                graphics2D.translate(0, -bufferedImage.getHeight() / 2d);

                // Draw the image onto the buffered image.
                graphics2D.drawImage(image, 0, 0, null);

                // Add the texture to the map.
                TEXTURES.put(name, new Texture(name, bufferedImage));
            }
        }

    }

    public static Texture getTexture(String name) {
        return TEXTURES.get(name);
    }

    public static void addTexture(String name, Texture texture) {
        TEXTURES.put(name, texture);
    }
}
