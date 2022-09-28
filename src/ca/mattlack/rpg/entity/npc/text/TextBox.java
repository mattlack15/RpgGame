package ca.mattlack.rpg.entity.npc.text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to create a text box on the screen.
 * It holds all the text and attributes that the text box will have
 * and contains the method to render the text box.
 */
public class TextBox {

    // The padding between the text and the border.
    private int padding;

    // The list of text elements that will be rendered. Check out TextBoxElement.java for more info.
    private List<TextBoxElement> text = new ArrayList<>();

    public TextBox(int padding) {
        this.padding = padding;
    }

    /**
     * Gets the list of text elements.
     */
    public List<TextBoxElement> getText() {
        return text;
    }

    /**
     * Sets the list of text elements.
     */
    public void setText(List<TextBoxElement> text) {
        this.text = text;
    }

    /**
     * Adds an element to the text element list.
     */
    public void addElement(TextBoxElement element) {
        text.add(element);
    }

    /**
     * Gets the padding between the text and the border.
     */
    public int getPadding() {
        return padding;
    }

    /**
     * Gets the width of the text box.
     */
    public int getWidth(Graphics2D graphics) {
        int maxWidth = 0;
        for (TextBoxElement element : text) {
            maxWidth = Math.max(maxWidth, element.getWidth(graphics));
        }
        return maxWidth + padding * 2;
    }

    /**
     * Gets the height of the text box.
     */
    public int getHeight(Graphics2D graphics2D) {
        int height = 0;
        for (TextBoxElement element : text) {
            height += element.getHeight(graphics2D);
        }
        return height + padding * 2;
    }

    /**
     * Draws the text box onto the specified graphics object.
     */
    public void draw(Graphics2D graphics2D) {

        // Dark pink border.
        graphics2D.setColor(new Color(0x964B00).darker());
        graphics2D.fillRoundRect(-8, -8, getWidth(graphics2D) + 16, getHeight(graphics2D) + 16, 16, 16);

        // Draw text space.
        graphics2D.setColor(new Color(0xffffe0));
        graphics2D.fillRoundRect(0, 0, getWidth(graphics2D), getHeight(graphics2D), 10, 10);

        // Initially have black text.
        graphics2D.setColor(Color.BLACK);

        // Draw each element on it's own line.
        int currentY = padding / 2;
        for (int i = 0; i < text.size(); i++) {

            TextBoxElement element = text.get(i);

            // Create a separate graphics object for the element.
            Graphics2D copy = (Graphics2D) graphics2D.create();

            // Increment the y position.
            currentY += element.getHeight(copy);

            // Translate the graphics object to the correct position.
            copy.translate(padding, currentY);

            // Draw the element.
            element.draw(copy);

        }
    }
}
