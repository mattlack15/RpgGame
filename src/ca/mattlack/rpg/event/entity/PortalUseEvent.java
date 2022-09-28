package ca.mattlack.rpg.event.entity;

import ca.mattlack.rpg.entity.Entity;
import ca.mattlack.rpg.event.Cancellable;
import ca.mattlack.rpg.world.Portal;

/**
 * This event is fired when a portal is used by an entity.
 * It is called BEFORE the entity is teleported and therefore
 * this event can be cancelled to prevent the teleportation.
 */
public class PortalUseEvent implements Cancellable {

    private final Entity entity;
    private final Portal portal;
    private boolean cancelled = false;

    public PortalUseEvent(Entity entity, Portal portal) {
        this.entity = entity;
        this.portal = portal;
    }

    public Entity getEntity() {
        return entity;
    }

    public Portal getPortal() {
        return portal;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
