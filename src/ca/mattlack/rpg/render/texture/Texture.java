package ca.mattlack.rpg.render.texture;

import java.awt.*;
import java.awt.image.PixelGrabber;

public class Texture {

    private final String id; // The id of this texture.

    private final Color averageColor; // The average color of this texture. This is used for the minimap.
    private final Image image; // The image for this texture.

    public Texture(String id, Image image) {
        this.id = id;
        this.image = image;

        // Calculate the average color.
        averageColor = getAverageColor(image);
    }

    public String getId() {
        return id;
    }

    public Color getAverageColor() {
        return averageColor;
    }

    public Image getImage() {
        return image;
    }

    private static Color getAverageColor(Image image) {

        int width = image.getWidth(null);
        int height = image.getHeight(null);

        // Get all the pixels from the image.
        int[] pixels = new int[width * height];
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);

        try {
            // Put the pixels in the array we made.
            pixelGrabber.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Accumulators for r g and b.
        int red = 0;
        int green = 0;
        int blue = 0;

        // Go through all the pixels and add their RGB values to the accumulators defined above.
        for (int pixel : pixels) {
            red += (pixel >> 16) & 0xFF;
            green += (pixel >> 8) & 0xFF;
            blue += pixel & 0xFF;
        }

        // Make them averages by dividing by the number of pixels.
        return new Color(red / pixels.length, green / pixels.length, blue / pixels.length);
    }
}
