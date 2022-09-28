package ca.mattlack.rpg.event.quest;

import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.quest.Objective;

/**
 * This event is fired when a player completes an objective.
 * It is called AFTER the objective has been completed so is
 * not cancellable.
 */
public class ObjectiveCompleteEvent
{
    private final Player player;
    private final Objective objective;

    public ObjectiveCompleteEvent(Player player, Objective objective)
    {
        this.player = player;
        this.objective = objective;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Objective getObjective()
    {
        return objective;
    }
}
