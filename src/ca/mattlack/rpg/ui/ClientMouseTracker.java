package ca.mattlack.rpg.ui;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.math.Vector2D;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClientMouseTracker extends MouseAdapter {
    private Client client;

    private boolean mousePressed = false; // Whether the mouse is currently pressed.
    private int mouseX = 0; // The x position of the mouse.
    private int mouseY = 0; // The y position of the mouse.

    public ClientMouseTracker(Client client) {
        this.client = client;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mousePressed = true; // Set the mouse pressed to true.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mousePressed = false; // Set the mouse pressed to false.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX(); // Set the mouse x position.
        this.mouseY = e.getY(); // Set the mouse y position.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e); // Call the mouse moved method as we want to treat the mouse as if it was moved.
    }

    public boolean isMousePressed() {
        return this.mousePressed;
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }

    public Vector2D getMousePosition() {
        return new Vector2D(this.mouseX, this.mouseY);
    }

    public Vector2D getMouseWorldPosition() {
        return this.client.getCamera().screenToWorldCoordinates(this.getMousePosition()); // Get the mouse position in world coordinates and return it.
    }


}
