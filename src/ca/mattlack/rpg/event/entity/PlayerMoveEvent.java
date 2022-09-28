package ca.mattlack.rpg.event.entity;

import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.event.Cancellable;
import ca.mattlack.rpg.math.Vector2D;

/**
 * This event is fired when a player moves.
 * It is called AFTER the player has been moved, however
 * it can still be cancelled to prevent the player from moving
 * as the player will be teleported back to the previous position
 * if the event is cancelled.
 */
public class PlayerMoveEvent implements Cancellable
{

    private final Player player;
    private final Vector2D from;
    private final Vector2D to;
    private boolean cancelled;

    public PlayerMoveEvent(Player player, Vector2D from, Vector2D to)
    {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public Player getPlayer() {
        return player;
    }

    public Vector2D getFrom()
    {
        return from;
    }

    public Vector2D getTo()
    {
        return to;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }
}
