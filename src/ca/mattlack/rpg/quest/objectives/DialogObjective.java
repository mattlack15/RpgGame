package ca.mattlack.rpg.quest.objectives;

import ca.mattlack.rpg.entity.npc.Dialog;
import ca.mattlack.rpg.entity.npc.EntityNPC;
import ca.mattlack.rpg.event.EventSubscription;
import ca.mattlack.rpg.event.entity.PlayerDialogAdvanceEvent;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.quest.GameQuest;
import ca.mattlack.rpg.quest.Objective;
import ca.mattlack.rpg.world.Location;

/**
 * And objective that is to complete some specified dialog with some specified npc.
 */
public class DialogObjective extends Objective
{

    private final EntityNPC npc; // The npc the objective requires the player to talk to.
    private final Dialog dialog; // The dialog to complete with the npc.
    private Dialog savedDialog; // The dialog saved from before this objective was started.

    public DialogObjective(GameQuest quest, String name, String description, EntityNPC npc, Dialog dialog)
    {
        super(quest, name, description);
        this.npc = npc;
        this.dialog = dialog;
    }



    @Override
    public void setup()
    {
        // Store the current dialog of the npc and set the npc's dialog to the dialog we need the player to complete with the npc.
        savedDialog = npc.getDialog();
        if (dialog != null) {
            npc.setDialog(dialog);
        }
    }

    @Override
    public void reverse()
    {
        npc.setDialog(savedDialog); // Reset the dialog back to what it was before this objective started.
    }

    @Override
    public Location getCurrentTarget()
    {
        return npc.getLocation(); // The target of this objective is the location of the npc.
    }

    @EventSubscription
    private void onDialog(PlayerDialogAdvanceEvent event) {
        if (event.getNpc() == npc && event.getNpc().getDialog().getCurrentElementIndex() + 1 >= event.getNpc().getDialog().getElements().size() - 1) {
            complete(); // Complete this objective when the player reaches the end of the dialog with the npc.
        }
    }
}
