package ca.mattlack.rpg.entity.npc.text;

import java.awt.*;

/**
 * This class is used in the TextBox class to represent a single text element. (Usually a single line of text)
 * The text can have it's own color and font.
 */
public class TextBoxElement {

    private String text;
    private Color color;
    private Font font;

    public static TextBuilder builder() {
        return new TextBuilder();
    }

    public TextBoxElement(String text, Color color, Font font) {
        this.text = text;
        this.color = color;
        this.font = font;
    }

    /**
     * Get's the text.
     */
    public String getText() {
        return text;
    }

    /**
     * Get's the color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get's the font.
     */
    public Font getFont() {
        return font;
    }

    /**
     * Get's the width.
     */
    public int getWidth(Graphics2D graphics2D) {
        return graphics2D.getFontMetrics(font).stringWidth(text);
    }

    /**
     * Get's the height.
     */
    public int getHeight(Graphics2D graphics2D) {
        return graphics2D.getFontMetrics(font).getHeight();
    }

    public void draw(Graphics2D graphics2D) {
        // Draw the text with the color and font set in this object's variables.
        graphics2D.setColor(color);
        graphics2D.setFont(font);
        graphics2D.drawString(text, 0, 0);
    }

    /**
     * Sets the text to display.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Sets the font of the text.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Sets the color of the text.
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
