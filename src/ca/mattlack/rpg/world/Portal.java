package ca.mattlack.rpg.world;

import ca.mattlack.rpg.entity.Entity;
import ca.mattlack.rpg.event.EventSubscriptions;
import ca.mattlack.rpg.event.entity.PortalUseEvent;

public class Portal {

    private long cooldown = -1; // The cooldown of the portal. -1 means it is not on cooldown. This is to prevent an infinite loop of portal usage.
    private Location entrance; // The location of the entrance of the portal.
    private Location exit; // The location of the exit of the portal.

    public Portal(Location entrance, Location exit) {
        this.entrance = entrance;
        this.exit = exit;
    }

    public Location getEntrance() {
        return entrance;
    }

    public Location getExit() {
        return exit;
    }

    public void teleport(Entity entity) {

        if (System.currentTimeMillis() < cooldown) { // If the portal is on cooldown, don't teleport the entity.
            return;
        }

        cooldown = System.currentTimeMillis() + 500; // Set the cooldown to 500 milliseconds.

        // Call the event.
        PortalUseEvent event = new PortalUseEvent(entity, this);
        EventSubscriptions.getInstance().publish(event);

        if (event.isCancelled()) { // If the event is cancelled, don't teleport the entity.
            return;
        }

        // Teleport the entity.
        entity.teleport(exit);
    }
}
