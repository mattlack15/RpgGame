package ca.mattlack.rpg.quest;

import ca.mattlack.rpg.event.EventSubscriptions;
import ca.mattlack.rpg.event.quest.ObjectiveCompleteEvent;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.world.Location;

/**
 * This class will represent something that the player has to do or complete.
 */
public abstract class Objective
{

    private final GameQuest quest; // The parent quest.
    private String name; // The name of this objective.
    private String description; // A description of this objective.

    // Whether or not this objective is active.
    private boolean active = false;

    // Something to run when this objective is completed.
    private Runnable onComplete;

    public Objective(GameQuest quest, String name, String description)
    {
        this.quest = quest;
        this.name = name;
        this.description = description;
    }

    public Objective whenCompleted(Runnable onComplete)
    {
        this.onComplete = onComplete;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public GameQuest getQuest()
    {
        return quest;
    }

    public boolean isActive()
    {
        return active;
    }

    public void activate()
    {
        // Set up the objective.
        setup();

        // Mark that this objective is now active.
        active = true;

        // Subscribe to events.
        EventSubscriptions.getInstance().subscribe(this, getClass());

    }

    public void deactivate()
    {
        // Mark that this objective is no longer active.
        active = false;

        // Unsubscribe from events as this objective is no longer active.
        EventSubscriptions.getInstance().unsubscribe(this, getClass());
    }

    public Location getCurrentTarget()
    {
        return null;
    }

    /**
     * Do everything that this objective needs in order to start.
     */
    public abstract void setup();

    /**
     * Undo everything this objective did in the setup function or while running. Effectively resetting this objective.
     */
    public abstract void reverse();

    public void tick() {}

    public void complete() {

        // Advance to the next quest objective.
        quest.advance();

        if (onComplete != null) {
            onComplete.run();
        }

        // Call the event.
        ObjectiveCompleteEvent event = new ObjectiveCompleteEvent(quest.getPlayer(), this);
        EventSubscriptions.getInstance().publish(event);
    }
}
