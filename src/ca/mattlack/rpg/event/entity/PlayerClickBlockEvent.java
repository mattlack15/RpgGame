package ca.mattlack.rpg.event.entity;

import ca.mattlack.rpg.event.Cancellable;
import ca.mattlack.rpg.world.WrappedBlock;

public class PlayerClickBlockEvent implements Cancellable
{
    public enum ClickType {
        LEFT,
        RIGHT,
        SHIFT_LEFT,
        SHIFT_RIGHT
    }

    private WrappedBlock block;
    private ClickType clickType;
    private boolean cancelled;

    public PlayerClickBlockEvent(WrappedBlock block, ClickType clickType)
    {
        this.block = block;
        this.clickType = clickType;
    }

    public WrappedBlock getBlock()
    {
        return block;
    }

    public ClickType getClickType()
    {
        return clickType;
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
