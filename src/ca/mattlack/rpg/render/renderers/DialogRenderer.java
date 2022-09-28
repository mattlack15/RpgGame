package ca.mattlack.rpg.render.renderers;

import ca.mattlack.rpg.entity.Entity;
import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.entity.npc.Dialog;
import ca.mattlack.rpg.entity.npc.DialogElement;
import ca.mattlack.rpg.entity.npc.EntityNPC;
import ca.mattlack.rpg.entity.npc.text.TextBox;
import ca.mattlack.rpg.entity.npc.text.TextBoxElement;
import ca.mattlack.rpg.entity.npc.text.TextBuilder;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderer;

import java.awt.*;
import java.io.FileNotFoundException;

/**
 * Render NPC dialog boxes to the screen.
 */
public class DialogRenderer extends Renderer {

    public DialogRenderer() {
        super("dialog");
    }

    @Override
    public void render(Graphics2D screen, PlayerCamera camera) {

        // Get the closest npc within a reasonable range.
        EntityNPC npc = getTargetNPC(camera.getPlayer());
        if (npc == null) {
            return;
        }

        // Get the dialog for the NPC.
        Dialog dialog = npc.getDialog();

        // If the npc doesn't have any dialog, then don't worry about displaying anything because there is nothing to display.
        if (dialog == null) return;

        // Get the current dialog element that the player is on.
        DialogElement element = dialog.getCurrentElement();
        if (element == null) return; // If the player is not on any dialog, don't bother displaying anything.
        if (element.getText() != null) { // If there is text to be displayed.

            String text = element.getText(); // Get the text to be displayed.
            boolean hasResponse = element.getResponse() != null; // Check if the player has a response to the dialog.

            Graphics2D graphics2D = (Graphics2D) screen.create();
            camera.transform(graphics2D, npc.getPosition().add(new Vector2D(1, 1.25)));

            // Need to flip the graphics upside down. This is because the camera is technically upside down so text is drawn upside down.
            graphics2D.scale(1, -1);

            TextBox textBox = new TextBox(10); // Create a new text box with 10 pixels of padding on all sides.

            // Add the text to the text box.
            TextBoxElement.builder()
                    .text(text)
                    .color(Color.BLACK)
                    .font(new Font("Arial", Font.BOLD, 16))
                    .buildWithNextLines()
                    .forEach(textBox::addElement);

            if (hasResponse) { // If the player has a response to the dialog.

                // Spacer.
                textBox.addElement(TextBoxElement.builder()
                        .text("")
                        .color(Color.BLACK)
                        .font(new Font("Arial", Font.PLAIN, 16))
                        .build());

                // Response.
                TextBoxElement.builder()
                        .text("You: " + element.getResponse())
                        .color(Color.DARK_GRAY)
                        .font(new Font("Arial", Font.PLAIN, 16))
                        .buildWithNextLines()
                        .forEach(textBox::addElement);

                // Footer.
                textBox.addElement(TextBoxElement.builder()
                        .text("Press enter to respond.")
                        .color(Color.GRAY.brighter())
                        .font(new Font("Arial", Font.PLAIN, 12))
                        .build());

            }

            int height = textBox.getHeight(graphics2D); // Get the height of the text box.

            graphics2D.translate(0, -height); // Translate the graphics object down (up on screen) to make room for the text box.

            graphics2D.setFont(new Font("Arial", Font.PLAIN, 16)); // Set the font for the text box.
            textBox.draw(graphics2D); // Draw the text box.

        }

    }

    public static EntityNPC getTargetNPC(Player player) {
        // For all NPCs within 5 blocks of the player, find the closest one.
        EntityNPC npc = null;
        double closestDistanceSquared = Double.MAX_VALUE;
        for (Entity value : player.getWorld().getEntities().values()) {
            if (value instanceof EntityNPC) {

                EntityNPC entity = (EntityNPC) value;
                double distanceSquared = player.getPosition().distanceSquared(entity.getPosition()); // Get the distance between the player and the NPC.

                if (distanceSquared < closestDistanceSquared) { // If the distance is closer than the current closest distance.
                    npc = entity; // Set the closest NPC.
                    closestDistanceSquared = distanceSquared; // Set the closest distance.
                }

            }
        }

        if (npc == null) return null; // If there is no NPC, return null.
        if (npc.getPosition().distanceSquared(player.getPosition()) > 49) return null; // If the NPC is too far away, return null.

        return npc; // Return the closest valid NPC.
    }

    @Override
    public int priority() {
        return 2; // Rendered on top of blocks and entities.
    }
}
