package ca.mattlack.rpg.quest;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.entity.EntityItem;
import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.entity.npc.Dialog;
import ca.mattlack.rpg.entity.npc.DialogElement;
import ca.mattlack.rpg.entity.npc.EntityNPC;
import ca.mattlack.rpg.event.EventSubscription;
import ca.mattlack.rpg.event.EventSubscriptions;
import ca.mattlack.rpg.event.entity.PortalUseEvent;
import ca.mattlack.rpg.event.entity.PlayerMoveEvent;
import ca.mattlack.rpg.item.ItemStack;
import ca.mattlack.rpg.item.ItemType;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.quest.objectives.DialogObjective;
import ca.mattlack.rpg.quest.objectives.FindObjective;
import ca.mattlack.rpg.quest.objectives.GetItemObjective;
import ca.mattlack.rpg.util.Serializer;
import ca.mattlack.rpg.world.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will handle the quest that the player needs to complete in the game.
 * There is only 1 quest so this class will handle most of the actual story of the game.
 */
public class GameQuest {

    private final Player player;

    // Worlds.
    private World mainWorld = Client.getInstance().getMainWorld(); // The main world.
    private World westernForestWorld = new World("Western Forest").register(); // The forest the wyland resides in.
    private World crystalCaveWorld = new World("Crystal Cave", 40, 40).register(); // The cave that the sword of vragon is in.

    // NPCs.
    private EntityNPC queenHeidi = new EntityNPC(mainWorld).setTextureId("queen-heidi");
    private EntityNPC jera = new EntityNPC(mainWorld);
    private EntityNPC wyland = new EntityNPC(westernForestWorld).setTextureId("wyland");

    private boolean canAccessCrystalCave = false;

    // Dialog.
    private Dialog queenHeidiDialog1 = new Dialog()
            .addElement(new DialogElement("Hello Path, I've heard a lot about you.", "You have?"))
            .addElement(new DialogElement("Yes, and I have a bit of a task for you\nso how would you like to service your Queen?", "What do you need?"))
            .addElement(new DialogElement("Our kingdom's seer has foresaw that a great force\nwill attack in a few days\nand we need you to find the Sword of Vragon\n and fight off the threat.",
                    "How will I be compensated?"))
            .addElement(new DialogElement("For starters, you can keep the sword.\n However on top of that, you'll be rewarded in Gold.", "I'll do it."))
            .addElement(new DialogElement("You'll need to find a wizard named Wyland.\nHe will lead you to it.", "Yes Ma'am..."))
            .addElement(new DialogElement("Thank you for your service.\nJera over there has a weapon that will help you\non your travels.", "Thank you."))
            .addElement(new DialogElement("Good luck on your travels.", null));

    private Dialog jeraDialog = new Dialog()
            .addElement(new DialogElement("I have a sword for you.", "Thank you."))
            .addElement(new DialogElement("You'll need this sword for your battles to come", null));

    private Dialog wylandDialog1 = new Dialog()
            .addElement(new DialogElement("What brings you to the\nforest of the Ancients?", "I'm looking for a weapon\nto help me on my journey."))
            .addElement(new DialogElement("What type of weapon do you need?", "I need a weapon called the Sword of Vragon."))
            .addElement(new DialogElement("Oh... I see.\nThat's a very powerful weapon.", "Yes."))
            .addElement(new DialogElement("The sword lives in the Crystal Cave.\nI have a map to the cave at my old hut.\nYou'll have to find it.", "Okay, where can I find that?"))
            .addElement(new DialogElement("Find my old hut in the northeast part of this forest.", null));

    private Dialog wylandDialog2 = new Dialog()
            .addElement(new DialogElement("Welcome back!", "Your hut looked like it was robbed.\nThe map is ripped into pieces."))
            .addElement(new DialogElement("Oh no!", "What will I do now?"))
            .addElement(new DialogElement("There is a spell, I can cast it.\nIt will take the pieces back in time\nto when they made the full map.", "Okay, what do you need?"))
            .addElement(new DialogElement("I need you to find me thyme leaves,\nand some water.", "Okay, I'll be back soon."));

    private Dialog wylandDialog3 = new Dialog()
            .addElement(new DialogElement("I see you found the ingredients I need.", "Yes they were scattered across the forest"))
            .addElement(new DialogElement("Good. Hand them over.", "Here you go."))
            .addElement(new DialogElement("*Wizard Sounds*", ""))
            .addElement(new DialogElement("Here you go!", "Thank you."));

    private Dialog queenHeidiDialog2 = new Dialog()
            .addElement(new DialogElement("Thank you for your service.", "I'm glad to have helped."))
            .addElement(new DialogElement("Game Complete!\n" +
                    "Thanks for playing!\n" +
                    "You can just walk around and explore now I guess\n\n" +
                    "------CREDITS------\n" +
                    "Code Developed by Matthew Lack\n" +
                    "Textures/Sprites by Mariana Cuan Celis", null));

    // List, in order of the objectives that the player needs to complete in this quest.
    private final List<Objective> objectiveList = new ArrayList<>();
    private int currentObjective = -1; // The objective that the player is currently completing.

    public GameQuest(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setup() {
        mainWorld = Client.getInstance().getMainWorld();

        // Setup the NPCs and teleport them to their respective locations.
        queenHeidi.setName("Queen Heidi");
        queenHeidi.setPosition(new Vector2D(110.5, 68.5));

        jera.setName("Jera");
        jera.setPosition(new Vector2D(95.5, 62.5));

        wyland.setName("Wyland");
        wyland.setPosition(new Vector2D(26, 32));

        // Make the objectives.
        objectiveList.add(new DialogObjective(this,
                "Starting Up",
                "Talk to Queen Heidi",
                queenHeidi,
                queenHeidiDialog1));

        objectiveList.add(new DialogObjective(this,
                "Weapons of Mass Destruction",
                "Talk to Jera",
                jera,
                jeraDialog).whenCompleted(() ->
        {
            // When this objective is completed, drop the sword.
            EntityItem item = new EntityItem(mainWorld, new ItemStack(ItemType.ROYAL_SWORD, 1));
            item.setPosition(jera.getPosition().add(0, 1.5));
        }));

        objectiveList.add(new FindObjective(this,
                "The Western Forest",
                "Find the western forest",
                new Location(mainWorld, new Vector2D(3, 18))));

        objectiveList.add(new DialogObjective(this,
                "The Wizard of the West",
                "Talk to Wyland",
                wyland,
                wylandDialog1));

        objectiveList.add(new FindObjective(this,
                "The wizard's old hut",
                "Find the old hut in the northeast part of the forest",
                new Location(westernForestWorld, new Vector2D(102, 108))));

        objectiveList.add(new GetItemObjective(this,
                "The Map",
                "Pick up the map fragments",
                ItemType.MAP_FRAGMENT, 12));

        objectiveList.add(new DialogObjective(this,
                "The Map Dilemma",
                "Talk to Wyland",
                wyland,
                wylandDialog2));

        objectiveList.add(new GetItemObjective(this,
                "Thyme",
                "Find thyme leaves",
                ItemType.THYME,
                1));

        objectiveList.add(new GetItemObjective(this,
                "Water",
                "Find some water by walking into a pond",
                ItemType.WATER_BOTTLE,
                1));

        objectiveList.add(new DialogObjective(this,
                "Back to the wizard",
                "Talk to Wyland",
                wyland,
                wylandDialog3)
                .whenCompleted(() ->
                {
                    // When this objective is completed:
                    // Take away the items.
                    player.getInventory().remove(ItemType.THYME, 1);
                    player.getInventory().remove(ItemType.WATER_BOTTLE, 1);
                    player.getInventory().remove(ItemType.MAP_FRAGMENT, 12);

                    // Give them a full map.
                    ItemStack map = new ItemStack(ItemType.MAP, 1);
                    player.getInventory().addItem(map);

                    // Allow them to access the crystal cave now.
                    canAccessCrystalCave = true;
                }));

        objectiveList.add(new FindObjective(this,
                "The Crystal Cave",
                "Find the crystal cave entrance",
                new Location(westernForestWorld, new Vector2D(18, 122))
        ));

        objectiveList.add(new GetItemObjective(this,
                "The Sword of Vragon",
                "Find the Sword of Vragon",
                ItemType.SWORD_OF_VRAGON,
                1));

        objectiveList.add(new DialogObjective(this,
                "The End",
                "Bring the sword to Queen Heidi",
                queenHeidi,
                queenHeidiDialog2));

        // Load the maps into the worlds.
        mainWorld.setMap(WorldMap.deserialize(new Serializer(new File("saves/kingdom.world"))));
        westernForestWorld.setMap(WorldMap.deserialize(new Serializer(new File("saves/westforest.world"))));
        crystalCaveWorld.setMap(WorldMap.deserialize(new Serializer(new File("saves/crystalcave.world"))));

        // Portals.
        Portal kingdomToWestForestPortal = new Portal(new Location(mainWorld, new Vector2D(1, 18)), new Location(westernForestWorld, new Vector2D(122, 50)));
        Portal westForestToKingdomPortal = new Portal(new Location(westernForestWorld, new Vector2D(128, 50)), new Location(mainWorld, new Vector2D(8, 18)));
        Portal westForestToCrystalCavePortal = new Portal(new Location(westernForestWorld, new Vector2D(17, 121)), new Location(crystalCaveWorld, new Vector2D(8, 8)));
        Portal crystalCaveToWestForestPortal = new Portal(new Location(crystalCaveWorld, new Vector2D(2, 2)), new Location(westernForestWorld, new Vector2D(9, 110)));

        // Register the portals.
        Client.getInstance().getPortalTracker().addPortal(kingdomToWestForestPortal);
        Client.getInstance().getPortalTracker().addPortal(westForestToKingdomPortal);
        Client.getInstance().getPortalTracker().addPortal(westForestToCrystalCavePortal);
        Client.getInstance().getPortalTracker().addPortal(crystalCaveToWestForestPortal);

        // Create the map fragment item entities.
        for (int i = 0; i < 12; i++) {
            EntityItem item = new EntityItem(westernForestWorld, new ItemStack(ItemType.MAP_FRAGMENT, 1));
            item.setPosition(new Vector2D(102 + Math.random() * 6 - 3, 108 + Math.random() * 6 - 3));
        }

        // Create the thyme leaves a 58, 80
        for (int i = 0; i < 8; i++) {
            EntityItem item = new EntityItem(westernForestWorld, new ItemStack(ItemType.THYME, 1));
            item.setPosition(new Vector2D(58 + Math.random() * 6 - 3, 77 + Math.random() * 6 - 3));
        }

        // Create and drop the sword of vragon in the crystal cave world at 30, 30.
        EntityItem swordOfVragon = new EntityItem(crystalCaveWorld, new ItemStack(ItemType.SWORD_OF_VRAGON, 1));
        swordOfVragon.setPosition(new Vector2D(27, 30));
    }

    /**
     * Start this quest. Starts the first objective and teleports the player to the start location.
     */
    public void start() {

        // Teleport the player to the starting position.
        player.teleport(new Location(mainWorld, new Vector2D(42, 18)));

        // Start the first objective.
        advance();

        // Subscribe to events.
        EventSubscriptions.getInstance().subscribe(this, getClass());
    }

    public void tick() {

        // tick the current event if it is active.
        Objective objective = getCurrentObjective();
        if (objective != null && objective.isActive()) {
            objective.tick();
        }

    }

    /**
     * Advance the player to the next objective.
     */
    public void advance() {
        if (currentObjective >= objectiveList.size() - 1) return; // No more objectives.

        if (currentObjective >= 0) {
            // Get the old objective, and deactivate it.
            Objective old = objectiveList.get(currentObjective);
            old.deactivate();
        }

        // Increment the currentObjective number.
        currentObjective++;

        // Get the new objective and activate it.
        Objective newObjective = objectiveList.get(currentObjective);
        newObjective.activate();
    }

    public void reverse() {
        if (currentObjective <= 0) return; // No more objectives.

        // Reverse the current objective and restart the previous objective.

        Objective old = objectiveList.get(currentObjective);
        old.deactivate(); // Deactivate the current objective.
        old.reverse(); // Reverse the current objective.

        currentObjective--;
        if (currentObjective >= 0) // If there is a previous objective, activate it.
        {
            Objective newObjective = objectiveList.get(currentObjective);
            newObjective.activate();
        }
    }

    /**
     * Get the current objective being performed in this quest.
     */
    public Objective getCurrentObjective() {
        if (currentObjective < 0) return null;
        if (currentObjective >= objectiveList.size()) return null;
        return objectiveList.get(currentObjective);
    }

    @EventSubscription
    private void onMove(PlayerMoveEvent event) {
        Block block = event.getPlayer().getWorld().getMap().getBlock(event.getPlayer().getPosition().toIntVector2D());
        if (block == Blocks.WATER) { // If the player walks into water, we want to give them a water bottle if they don't already have one.
            if (event.getPlayer().getInventory().count(ItemType.WATER_BOTTLE) < 1) { // If they don't already have one.
                event.getPlayer().getInventory().addItem(new ItemStack(ItemType.WATER_BOTTLE, 1)); // Give them 1 water bottle.
            }
        }
    }

    @EventSubscription
    private void onPortal(PortalUseEvent event) {
        if (event.getPortal().getExit().getWorld() == crystalCaveWorld) {
            if (!canAccessCrystalCave) { // Don't allow access to the crystal cave until the player has completed the right objectives.
                event.setCancelled(true);
            }
        }
    }
}
