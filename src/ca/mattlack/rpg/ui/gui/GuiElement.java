package ca.mattlack.rpg.ui.gui;

import java.awt.*;

public abstract class GuiElement {

    public int x;
    public int y;
    public int width;
    public int height;

    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public abstract void draw(Graphics2D g);

    /**
     * Called when this element is clicked.
     */
    public void clicked() {}
}
