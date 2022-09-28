package ca.mattlack.rpg.event;

/**
 * This interface is used to make events cancellable.
 */
public interface Cancellable
{
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
