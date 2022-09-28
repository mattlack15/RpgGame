package ca.mattlack.rpg;

import ca.mattlack.rpg.event.EventSubscription;
import ca.mattlack.rpg.event.EventSubscriptions;
import ca.mattlack.rpg.event.entity.PlayerMoveEvent;
import ca.mattlack.rpg.world.Portal;

import java.util.ArrayList;
import java.util.List;

public class PortalTracker {

    private final Client client;

    private final List<Portal> portals = new ArrayList<>(); // List of all active portals.

    public PortalTracker(Client client) {
        this.client = client;

        // Subscribe to events.
        EventSubscriptions.getInstance().subscribe(this, getClass());
    }

    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    public void removePortal(Portal portal) {
        portals.remove(portal);
    }

    public List<Portal> getPortals() {
        return portals;
    }

    @EventSubscription
    private void onMove(PlayerMoveEvent event) {

        // When a player moves, go through the portals and check if the player is in the entrance.
        // If they are, teleport them to the exit.
        for (Portal portal : portals) {
            if (portal.getEntrance().getWorld() != event.getPlayer().getWorld()) continue; // If the player is not in the same world as this portal, skip this portal.

            // If the player is in the entrance (within 3 blocks), teleport them to the exit.
            if (portal.getEntrance().getPosition().distanceSquared(event.getPlayer().getPosition()) < 9) {
                portal.teleport(event.getPlayer()); // Teleport the player to the exit.
            }
        }
    }
}
