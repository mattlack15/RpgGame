package ca.mattlack.rpg;

import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.quest.GameQuest;
import ca.mattlack.rpg.render.GameRenderer;
import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderers;
import ca.mattlack.rpg.render.texture.Textures;
import ca.mattlack.rpg.ui.ClientKeyboardTracker;
import ca.mattlack.rpg.ui.ClientMouseTracker;
import ca.mattlack.rpg.ui.gui.GuiTracker;
import ca.mattlack.rpg.world.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Adventures of Path.
 *
 * This is a program that creates a game environment where you can move around and explore a town and a forest. The game
 * gives you objectives to complete and on a larger scale, quest to complete and to finish the game. The quest is to
 * serve the queen by finding a secret sword in a crystal cave. You will meet various npcs on the jouney and will need to complete
 * different objectives to complete the quest. You can press enter to move through dialog with npcs.
 *
 * Matthew Lack
 * 2022-06-17
 */
public class Client
{

    private static Client instance;

    public static Client getInstance()
    {
        return instance;
    }

    private final List<World> worldList = new ArrayList<>();

    private final World mainWorld = new World("Main"); // The main world, and by default is just blank and 128x128.
    private final Player player = new Player(mainWorld, UUID.randomUUID(), "Path");  // The main character. A player.
    private final PlayerCamera camera = new PlayerCamera(player, 0, 0); // The camera for the game, it follows the player.

    private final GameRenderer gameRenderer = new GameRenderer(camera); // The game renderer, handles all rendering.
    private final ClientKeyboardTracker keyboardTracker = new ClientKeyboardTracker(this); // The keyboard tracker, handles all keyboard input.
    private final ClientMouseTracker mouseTracker = new ClientMouseTracker(this); // The mouse tracker, handles all mouse input.

    private final PortalTracker portalTracker = new PortalTracker(this); // The portal tracker, handles all portals.

    private final GuiTracker guiTracker = new GuiTracker(this); // The GUI tracker, handles all GUI input, and rendering.

    private final PlayerControls controls; // The player controls, handles all player input using the keyboard and mouse trackers.

    private Block placingBlock = null; // The block that can be placed by the player if they click on a block.

    private long tick = 0; // The current tick number.

    // Used for creating the maps.
//    private WorldEditHandler handler = new WorldEditHandler();

    // Main method, starts the client.
    public static void main(String[] args)
    {
        new Client();
    }

    public Client()
    {
        instance = this; // Set the instance.

        gameRenderer.setup(); // Setup the game renderer, this will create thw window etc.

        gameRenderer.getFrame().addKeyListener(keyboardTracker); // Add the keyboard tracker as a key listener.
        gameRenderer.getFrame().addMouseListener(mouseTracker); // Add the mouse tracker as a mouse listener.
        gameRenderer.getFrame().addMouseMotionListener(mouseTracker); // Add the mouse tracker as a mouse motion listener as well.

        Textures.loadTextures(new File("textures")); // Load all the textures in the textures folder.

        worldList.add(mainWorld); // Add the main world to the world list.

        controls = new PlayerControls(this); // Create the player controls object.

        Renderers.ENTITY.getClass(); // Load the renderer classes. All this does is make sure the Renderers class in initialized and the static final fields are loaded.
        Blocks.AIR.getTexture(); // Load the block textures. Same thing happening here.
        WorldObjectTypes.loadClassByFunc(); // Load the world object types. Same thing happening here. The function is blank, it's just to load the class and therefore it's fields too.

        player.setPosition(new Vector2D(0, 0)); // Set the player's position to 0, 0.
        player.getInventoryGui().setX((gameRenderer.getFrame().getWidth() - player.getInventoryGui().getWidth()) / 2); // Set the player's inventory GUI's x position to the middle of the screen.
        player.getInventoryGui().setY((gameRenderer.getFrame().getHeight() - player.getInventoryGui().getHeight() - 10)); // Set the player's inventory GUI's y position to the bottom of the screen.

        GameQuest quest = new GameQuest(player); // Create a new quest instance for the player. This will handle most of the storyline gameplay.
        player.setQuest(quest); // Set the player's quest to the quest instance.
        quest.setup(); // Setup the quest. This will create the quest's objectives and spawn the quest's items and NPCs.
        quest.start(); // Start the quest. This will start the first objective and teleport the player to the starting location.

        loop();
    }

    public void loop()
    {

        long lastTick = System.currentTimeMillis(); // Keep track of when the last tick happened.
        long lastFrame = System.currentTimeMillis(); // Keep track of when the last frame happened.

        int frames = 0; // Keep track of the number of frames that have happened in the last second.
        long lastFrameCount = System.currentTimeMillis(); // Keep track of when the last frame counter reset happened.

        while (true)
        {

            int ticksPerSecond = 40; // The number of ticks that should happen in a second.
            int tickDurationMs = 1000 / ticksPerSecond; // The duration of a tick in milliseconds.

            int targetFrameRate = 200; // The target frame rate.

            long ns = System.nanoTime(); // Get the current time in nanoseconds.

            // Update entity positions.
            // Delta is a modifier that is used to make sure that overall an
            // entity's position is changed at the same rate no matter the frame rate.
            double delta = (System.currentTimeMillis() - lastFrame) / (double) tickDurationMs;
            worldList.forEach(world -> world.updatePositions(delta));
            lastFrame = System.currentTimeMillis(); // Update the last frame time.

            // Tick?
            if (System.currentTimeMillis() - lastTick >= tickDurationMs) // If the last tick happened more than the tick duration in milliseconds in the past.
            {
                worldList.forEach(World::tick); // Tick the world.
                GameQuest quest = player.getQuest();
                if (quest != null) {
                    quest.tick(); // Tick the quest.
                }

                lastTick = System.currentTimeMillis(); // Update the last tick time.
                tick++; // Increment the tick number.
            }

            // Render.
            gameRenderer.render(); // Render the game to the screen.

            // Controls.
            handleControls(delta); // Handle the player's controls.

            // Sleep
            // Calculate how long in milliseconds and how many extra nanoseconds the thread should sleep for.
            ns = System.nanoTime() - ns;
            long sleepNs = 1000000000 / targetFrameRate - ns;
            long sleepMs = sleepNs / 1000000;
            sleepNs = sleepNs % 1000000;

            // Make sure they're not negative.
            sleepMs = Math.max(0, sleepMs);
            sleepNs = Math.max(0, sleepNs);

            try
            {
                Thread.sleep(sleepMs, (int) sleepNs); // Sleep for that long.
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            // Frame rate.
            if (System.currentTimeMillis() - lastFrameCount >= 1000)
            {
                System.out.println("FPS: " + frames);
                frames = 0;
                lastFrameCount = System.currentTimeMillis();
            }
            frames++;
        }
    }

    // A bunch of mutators and accessors.
    // Their names explain their function.

    public List<World> getWorldList() {
        return worldList;
    }

    public void addWorld(World world)
    {
        worldList.add(world);
    }

    public void removeWorld(World world)
    {
        worldList.remove(world);
    }

    /**
     * Ticks the player control handler.
     */
    public void handleControls(double delta)
    {
        controls.tick(delta);
    }

    public ClientKeyboardTracker getKeyboardTracker()
    {
        return keyboardTracker;
    }

    public ClientMouseTracker getMouseTracker()
    {
        return mouseTracker;
    }

    public PlayerCamera getCamera()
    {
        return camera;
    }

    public GuiTracker getGuiTracker()
    {
        return guiTracker;
    }

    public void setPlacingBlock(Block placingBlock)
    {
        this.placingBlock = placingBlock;
    }

    public Block getPlacingBlock()
    {
        return placingBlock;
    }

    public GameRenderer getGameRenderer()
    {
        return gameRenderer;
    }

    public long getTick() {
        return tick;
    }

    public PortalTracker getPortalTracker() {
        return portalTracker;
    }

    public World getMainWorld()
    {
        return mainWorld;
    }

    public Player getPlayer()
    {
        return player;
    }

}

