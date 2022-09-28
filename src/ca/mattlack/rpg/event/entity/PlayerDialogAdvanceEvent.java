package ca.mattlack.rpg.event.entity;

import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.entity.npc.DialogElement;
import ca.mattlack.rpg.entity.npc.EntityNPC;
import ca.mattlack.rpg.event.Cancellable;

/**
 * This event is fired when a player advances to the next dialog element in a dialog.
 */
public class PlayerDialogAdvanceEvent implements Cancellable
{
    private final Player player;
    private final EntityNPC npc;
    private final DialogElement nextDialogElement;
    private boolean cancelled = false;

    public PlayerDialogAdvanceEvent(Player player, EntityNPC npc)
    {
        this.player = player;
        this.npc = npc;

        if (npc.getDialog().getCurrentElementIndex() + 1 >= npc.getDialog().getElements().size()) {
            nextDialogElement = npc.getDialog().getCurrentElement();
        } else {
            nextDialogElement = npc.getDialog().getElements().get(npc.getDialog().getCurrentElementIndex() + 1);
        }
    }

    public Player getPlayer()
    {
        return player;
    }

    public EntityNPC getNpc()
    {
        return npc;
    }

    public DialogElement getNextDialogElement()
    {
        return nextDialogElement;
    }

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
