package ca.mattlack.rpg.quest.objectives;

import ca.mattlack.rpg.event.EventSubscription;
import ca.mattlack.rpg.event.entity.PlayerMoveEvent;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.quest.GameQuest;
import ca.mattlack.rpg.quest.Objective;
import ca.mattlack.rpg.world.Location;

/**
 * Objective to find a specific location in the game.
 */
public class FindObjective extends Objective {

    private final Location location; // The location the find.

    public FindObjective(GameQuest quest, String name, String description, Location location) {
        super(quest, name, description);
        this.location = location;
    }

    @Override
    public void setup() {

    }

    @Override
    public void reverse() {

    }

    @Override
    public Location getCurrentTarget()
    {
        return location;
    }

    @EventSubscription
    private void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().getWorld() != location.getWorld()) {
            return;
        }

        if (event.getPlayer().getPosition().distanceSquared(location.getPosition()) < 25) {
            complete(); // If the player is within 5 blocks of the target location, then complete this objective.
        }
    }
}
