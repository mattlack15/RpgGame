package ca.mattlack.rpg.render;

import ca.mattlack.rpg.entity.npc.text.TextBox;
import ca.mattlack.rpg.entity.npc.text.TextBoxElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class GameRenderer {
    public static final int SCALE = 25; // The scale of 1 block in the game.

    private final JFrame frame = new JFrame("The Adventures of Path"); // The window.
    private final PlayerCamera camera; // The camera.

    public GameRenderer(PlayerCamera camera) {
        this.camera = camera;
    }

    public void setup() {
        frame.setSize(1000, 800); // Set the size of the window.

        camera.setViewWidth(frame.getWidth()); // Set the camera's view width.
        camera.setViewHeight(frame.getHeight()); // Set the camera's view height.

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation.

        frame.setVisible(true); // Make the window visible.

        frame.createBufferStrategy(2); // Create a buffer strategy (double buffering).

        renderTitleScreen();

        CompletableFuture<?> titleScreenFinished = new CompletableFuture<>();

        frame.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                titleScreenFinished.complete(null);
            }
        });

        titleScreenFinished.join();
    }

    public JFrame getFrame() {
        return frame;
    }

    public void renderTitleScreen() {
        Graphics2D graphics2D = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();

        graphics2D.setBackground(Color.BLACK);
        graphics2D.clearRect(0, 0, frame.getWidth(), frame.getHeight());

        Font font = new Font("Arial", Font.PLAIN, 20);

        TextBox box = new TextBox(20);
        TextBoxElement.builder()
                .color(Color.BLACK)
        .font(font)
        .text("Welcome to The Adventures of Path\n" +
                "When you spawn in you will be in a town and will need\n" +
                "to follow objectives to complete the game.\n" +
                "You can see your objective in the top left of the screen.\n" +
                "You can see a minimap in the top right of the screen.\n" +
                "You inventory is on the bottom of your screen with a\n" +
                "health bar which you will not need.\n" +
                "\n" +
                "You can move around using the WASD keys. W moves forwards,\n" +
                "A moves left, S moves backwards, and D moves right.\n" +
                "If you want to cheat a little bit, you can press E\n" +
                "to select a block and click the ground afterwards to place\n" +
                "that block wherever the mouse is.\n" +
                "\n" +
                "Items are on the ground for some objectives and you\n" +
                "can pick them up by walking over them.\n" +
                        "When you talk to an NPC you can advance to the next piece\n" +
                "of dialog by hitting the enter key.\n" +
                "Portals will teleport you to different parts of the game\n" +
                "they are marked by block squares.\n" +
                "\n" +
                "Press any key to continue to the game.")
        .buildWithNextLines().forEach(box::addElement);

        graphics2D.translate(160, 150);
        box.draw(graphics2D);

        frame.getBufferStrategy().show();
        graphics2D.dispose();
    }

    public void render() {
        Graphics2D graphics2D = (Graphics2D) frame.getBufferStrategy().getDrawGraphics();

        // Anti-aliasing on.
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.setBackground(new Color(0x87ceeb)); // Sky blue background.
        graphics2D.clearRect(0, 0, frame.getWidth(), frame.getHeight());

        List<Renderer> rendererList = new ArrayList<>(Renderer.REGISTRY.values());
        rendererList.sort(Comparator.comparingInt(Renderer::priority)); // Sort the renderer list by priority.

        for (Renderer renderer : rendererList) { // Render each renderer.
            renderer.render(graphics2D, camera);
        }

        frame.getBufferStrategy().show(); // Show the buffer we just rendered to.
        graphics2D.dispose(); // Dispose of the graphics2D object.
    }
}
