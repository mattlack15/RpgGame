package ca.mattlack.rpg.ui.gui;

import ca.mattlack.rpg.Client;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Gui {

    GuiTracker tracker;

    private int x = 0;
    private int y = 0;

    private int width;
    private int height;

    private boolean visible = false;

    private List<GuiElement> elements = new ArrayList<>();

    public Gui(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Remove this gui from the tracker.
     */
    public void remove() {
        if (tracker != null) {
            tracker.removeGui(this); // Remove this gui from the tracker.
        }
    }

    /**
     * Adds the gui to the gui tracker.
     */
    public void add() {
        if (tracker == null) { // If the tracker is null, we need to set our tracker to the default tracker (the one in the client instance).
            tracker = Client.getInstance().getGuiTracker();
        }
        tracker.addGui(this); // Add this gui to the gui tracker.
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<GuiElement> getElements() {
        return elements;
    }

    public void addElement(GuiElement element) {
        elements.add(element);
    }

    public void removeElement(GuiElement element) {
        elements.remove(element);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void draw(Graphics2D g) {
        for (GuiElement element : elements) { // Draw all the elements.
            Graphics2D g2 = (Graphics2D) g.create(); // Create a new graphics object.
            g2.translate(element.getX(), element.getY()); // Translate the graphics object to the element's x and y.
            element.draw(g2); // Draw the element.
        }
    }

    public void click(int x, int y) {
        for (GuiElement element : elements) {
            if (element.x <= x && element.x + element.width >= x && element.y <= y && element.y + element.height >= y) { // If the click is within the bounds of the element.
                // Clicked on element so call the click function.
                element.clicked();
            }
        }
    }
}
