package ca.mattlack.rpg.ui;

import ca.mattlack.rpg.Client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientKeyboardTracker extends KeyAdapter {
    private final Client client;

    // The set of keys that are currently pressed.
    // This must be a concurrent set because key events
    // are asynchronous.
    private final Set<Integer> pressedKeys = ConcurrentHashMap.newKeySet();

    public ClientKeyboardTracker(Client client) {
        this.client = client;
    }

    public boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public void setPressed(int keyCode, boolean pressed) {
        if (pressed) {
            pressedKeys.add(keyCode);
        } else {
            pressedKeys.remove(keyCode);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode()); // Add the key to the set of pressed keys.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode()); // Remove the key from the set of pressed keys.
    }
}
