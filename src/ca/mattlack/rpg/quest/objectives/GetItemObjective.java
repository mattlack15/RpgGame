package ca.mattlack.rpg.quest.objectives;

import ca.mattlack.rpg.event.EventSubscription;
import ca.mattlack.rpg.item.Inventory;
import ca.mattlack.rpg.item.ItemType;
import ca.mattlack.rpg.quest.GameQuest;
import ca.mattlack.rpg.quest.Objective;

/**
 * Objective to acquire the items specified in the constructor of this objective.
 */
public class GetItemObjective extends Objective {

    private final ItemType itemType; // The type of item.
    private final int amount; // The amount the player is required to get.

    public GetItemObjective(GameQuest quest, String name, String description, ItemType itemType, int amount) {
        super(quest, name, description);

        this.itemType = itemType;
        this.amount = amount;
    }

    @Override
    public void setup() {

    }

    @Override
    public void reverse() {

    }

    @Override
    public void tick() {
        Inventory inventory = getQuest().getPlayer().getInventory(); // Every tick, get the player's inventory.
        if (inventory.count(itemType) >= amount) { // Check if the amount of the type of item we are looking for is above or equal to the amount the player needs to get to complete this objective.
            complete(); // If so, complete this objective.
        }
    }
}
