package ca.mattlack.rpg;

import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.entity.npc.EntityNPC;
import ca.mattlack.rpg.event.EventSubscriptions;
import ca.mattlack.rpg.event.entity.PlayerClickBlockEvent;
import ca.mattlack.rpg.event.entity.PlayerDialogAdvanceEvent;
import ca.mattlack.rpg.event.entity.PlayerMoveEvent;
import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.render.renderers.DialogRenderer;
import ca.mattlack.rpg.ui.ClientKeyboardTracker;
import ca.mattlack.rpg.ui.ClientMouseTracker;
import ca.mattlack.rpg.ui.blockselector.BlockSelectorGui;
import ca.mattlack.rpg.ui.gui.Gui;
import ca.mattlack.rpg.util.Serializer;
import ca.mattlack.rpg.world.WorldMap;
import ca.mattlack.rpg.world.WrappedBlock;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class PlayerControls {

    private Client parent; // The parent client.
    private ClientMouseTracker mouse; // The mouse tracker.
    private ClientKeyboardTracker keyboard; // The keyboard tracker.

    private BlockSelectorGui blockSelectorGui; // The block selector GUI.

    private boolean mouseWasPressed; // Whether the mouse was pressed last tick. This is used to determine when the player first clicks.

    // Flag for whether or not we should place a block where the player's mouse is.
    private boolean placing = false;

    // Flags for whether or not we are currently trying to save or load a map.
    private boolean saving = false;
    private boolean loading = false;

    public PlayerControls(Client parent) {
        this.parent = parent;
        this.mouse = parent.getMouseTracker();
        this.keyboard = parent.getKeyboardTracker();
        this.blockSelectorGui = new BlockSelectorGui(parent);

        // Block Selector GUI.
        // Make sure it's added to the gui tracker.
        parent.getGuiTracker().addGui(blockSelectorGui);

        // Move it to the center of the screen.
        blockSelectorGui.setX((parent.getGameRenderer().getFrame().getWidth() - blockSelectorGui.getWidth()) / 2);
        blockSelectorGui.setY((parent.getGameRenderer().getFrame().getHeight() - blockSelectorGui.getHeight()) / 2);

    }

    public void tick(double delta) {
        // Handle mouse input.
        handleMouse();

        // Handle keyboard input.
        handleKeyboard(delta);
    }

    public void handleMouse() {

        // If the mouse wasn't pressed a tick ago, and it is now, then it's a single-event click.
        if (!mouseWasPressed && mouse.isMousePressed()) {
            // Clicked (Single event)

            // See if the player clicked on any guis.
            if (!parent.getGuiTracker().click(mouse.getMouseX(), mouse.getMouseY())) {

                // If they didn't, then they clicked in the world.
                Vector2D mouseWorldPosition = mouse.getMouseWorldPosition();
                IntVector2D mouseBlockPosition = mouseWorldPosition.toIntVector2D();

                // Call the event.
                PlayerClickBlockEvent.ClickType clickType = PlayerClickBlockEvent.ClickType.LEFT;
                PlayerClickBlockEvent event = new PlayerClickBlockEvent(new WrappedBlock(mouseBlockPosition, parent.getPlayer().getWorld()), clickType);
                EventSubscriptions.getInstance().publish(event);

                // If the event was cancelled, then we don't want to do anything else.
                // We don't want to start placing blocks.
                // But if the event wasn't cancelled then we
                // should start placing blocks at the player's mouse position.
                placing = !event.isCancelled();
            } else {
                // If they clicked on a gui, then we don't want to start placing blocks.
                placing = false;
            }
        }

        if (mouse.isMousePressed() && placing) // If the mouse is pressed and we should be placing blocks, then we need to place a block.
        {

            // Get where in the world the mouse was clicked.
            Vector2D mouseWorldPosition = mouse.getMouseWorldPosition();

            // Get that as a block position.
            IntVector2D mouseBlockPosition = mouseWorldPosition.toIntVector2D();

            // Set the block at that position to the block that the client has as the placingBlock field.
            parent.getPlayer().getWorld().getMap().setBlock(mouseBlockPosition, parent.getPlacingBlock());
        }

        // Update the mouseWasPressed flag.
        mouseWasPressed = mouse.isMousePressed();
    }

    public void handleKeyboard(double delta) {

        Player player = parent.getPlayer(); // Get the player.

        double speed = 0.3 * delta * (keyboard.isPressed(KeyEvent.VK_SHIFT) ? 1 : 1.75); // The speed will be 0.3 blocks per tick. So ~12 blocks a second.

        // This will be a vector that will represent what directions the player wants to move in.
        Vector2D direction = new Vector2D(0, 0);

        // If two opposite directional keys are pressed, they will cancel out as -1 + 1 = 0
        if (keyboard.isPressed(KeyEvent.VK_W)) {
            direction = direction.add(new Vector2D(0, 1));
        }
        if (keyboard.isPressed(KeyEvent.VK_S)) {
            direction = direction.add(new Vector2D(0, -1));
        }
        if (keyboard.isPressed(KeyEvent.VK_A)) {
            direction = direction.add(new Vector2D(-1, 0));
        }
        if (keyboard.isPressed(KeyEvent.VK_D)) {
            direction = direction.add(new Vector2D(1, 0));
        }

        if (direction.length() > 0) // If they don't want to stay still.
        {
            // Normalize the direction vector so it has length 1.
            direction = direction.normalize();

            // Save the position of the player for if the event is cancelled.
            Vector2D prevPlayerPosition = player.getPosition();

            // Move them in that direction and by a distance of speed.
            player.move(direction.multiply(speed));

            // Get the new position of the player.
            Vector2D newPlayerPosition = player.getPosition();

            // Call the event.
            PlayerMoveEvent event = new PlayerMoveEvent(player, prevPlayerPosition, newPlayerPosition);
            EventSubscriptions.getInstance().publish(event);

            if (event.isCancelled()) {
                // If the event was cancelled, then we need to set the player's position back to where it was before.
                player.setPosition(prevPlayerPosition);
            }
        }

        if (keyboard.isPressed(KeyEvent.VK_ESCAPE)) { // If the player presses escape, then we need to close whatever inventory is being shown.
            for (Gui gui : parent.getGuiTracker().getGuis()) {
                if (gui.isVisible()) { // If the gui is visible, then we need to close it.
                    gui.setVisible(false); // Close the gui.
                    break;
                }
            }
        }

        // Specifically, the block selector gui should be visible ONLY when pressing E.
        blockSelectorGui.setVisible(keyboard.isPressed(KeyEvent.VK_E));

        // If the player is pressing CTRL + SHIFT + S, then they want to save the map.
        if (keyboard.isPressed(KeyEvent.VK_S) && keyboard.isPressed(KeyEvent.VK_CONTROL) && keyboard.isPressed(KeyEvent.VK_SHIFT)) {
            // If we're already saving, then skip this.
            if (!saving) {
                saving = true; // Set the saving flag to true as we're now saving.

                String result = JOptionPane.showInputDialog("Save as: "); // Get the name of the file to save as with a prompt.
                if (result != null) {
                    File file = new File("saves/" + result + ".world"); // Create a file object with the name of the file.

                    parent.getPlayer().getWorld().getMap().serialize().write(file); // Write the map to the file.
                    JOptionPane.showMessageDialog(null, "Saved."); // Show a message to the player saying that the map was saved.
                }

                // Reset the keys.
                keyboard.setPressed(KeyEvent.VK_S, false);
                keyboard.setPressed(KeyEvent.VK_CONTROL, false);
                keyboard.setPressed(KeyEvent.VK_SHIFT, false);
            }
        } else {
            saving = false; // Set the saving flag to false as we're not saving.
        }

        // If the player is pressing CTRL + SHIFT + L, then they want to load a map.
        if (keyboard.isPressed(KeyEvent.VK_L) && keyboard.isPressed(KeyEvent.VK_CONTROL) && keyboard.isPressed(KeyEvent.VK_SHIFT)) {
            // If we're already loading, then skip this.
            if (!loading) {
                loading = true; // Set the loading flag to true as we're now loading.
                String result = JOptionPane.showInputDialog("Load: "); // Get the name of the file to load with a prompt.
                if (result != null) {
                    File file = new File("saves/" + result + ".world"); // Create a file object with the name of the file.
                    if (file.exists()) // If the file exists, then we can load it.
                    {

                        WorldMap map = WorldMap.deserialize(new Serializer(file)); // Deserialize the map from the file.
                        parent.getPlayer().getWorld().setMap(map); // Set the world's map to the deserialized map.

                    } else {
                        JOptionPane.showMessageDialog(null, "File does not exist."); // Show a message to the player saying that the file doesn't exist.
                    }

                }

                // Reset the keys.
                keyboard.setPressed(KeyEvent.VK_L, false);
                keyboard.setPressed(KeyEvent.VK_CONTROL, false);
                keyboard.setPressed(KeyEvent.VK_SHIFT, false);
            }
        } else {
            loading = false; // Set the loading flag to false as we're not loading.
        }

        // If the player is pressing enter, then they want to advance in their open dialog with an NPC.
        if (keyboard.isPressed(KeyEvent.VK_ENTER)) { // If the player presses enter.
            EntityNPC targetNPC = DialogRenderer.getTargetNPC(player); // Get the NPC that the player is currently talking to.
            if (targetNPC != null) { // If the player is talking to an NPC.
                keyboard.setPressed(KeyEvent.VK_ENTER, false); // Reset the key.

                PlayerDialogAdvanceEvent event = new PlayerDialogAdvanceEvent(player, targetNPC); // Create a new event.
                EventSubscriptions.getInstance().publish(event); // Publish the event.

                if (!event.isCancelled()) { // If the event wasn't cancelled.
                    targetNPC.getDialog().nextElement(); // Advance to the next element in the dialog.
                }
            }
        }

        // If the player is holding the A key, then they should be facing to the left.
        player.setFacingRight(!keyboard.isPressed(KeyEvent.VK_A));
    }
}
