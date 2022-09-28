package ca.mattlack.rpg.entity.npc.text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder utility class for creating a text box element.
 */
public class TextBuilder {

    // Create a new text box element with default values.
    private TextBoxElement element = new TextBoxElement("", Color.BLACK, new Font("Arial", Font.PLAIN, 12));

    /**
     * Sets the text of the text box element.
     *
     * @param text The text to set.
     * @return The text builder.
     */
    public TextBuilder text(String text) {
        element.setText(text);
        return this;
    }

    /**
     * Sets the color of the text box element.
     *
     * @param color The color to set.
     * @return The text builder.
     */
    public TextBuilder color(Color color) {
        element.setColor(color);
        return this;
    }

    /**
     * Sets the font of the text box element.
     *
     * @param font The font to set.
     * @return The text builder.
     */
    public TextBuilder font(Font font) {
        element.setFont(font);
        return this;
    }

    /**
     * Builds the text box element.
     *
     * @return The text box element.
     */
    public TextBoxElement build() {
        return element;
    }

    /**
     * Builds the text box element, splitting the text into multiple lines.
     *
     * @return The text box elements.
     */
    public List<TextBoxElement> buildWithNextLines() {
        List<TextBoxElement> elements = new ArrayList<>();
        for (String line : element.getText().split("\n")) {
            elements.add(new TextBoxElement(line, element.getColor(), element.getFont()));
        }
        return elements;
    }
}
