package ca.mattlack.rpg.world.edit;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.event.EventSubscription;
import ca.mattlack.rpg.event.EventSubscriptions;
import ca.mattlack.rpg.event.entity.PlayerClickBlockEvent;
import ca.mattlack.rpg.math.IntVector2D;
import ca.mattlack.rpg.world.WorldMap;
import ca.mattlack.rpg.world.WrappedBlock;

/**
 * Literally don't worry about this class it was only used during the making of the maps. It has no function in the final product of the game.
 */
public class WorldEditHandler
{

    private WorldMap clipboard;

    private IntVector2D position1;
    private IntVector2D position2;

    public WorldEditHandler()
    {
        EventSubscriptions.getInstance().subscribe(this, WorldEditHandler.class);
    }

    @EventSubscription
    private void onClickBlock(PlayerClickBlockEvent event) {
        System.out.println("Click block event");
        WrappedBlock block = event.getBlock();
        if (position1 == null) {
            position1 = block.getPosition();
            System.out.println("Position 1 set.");
        } else {
            position2 = block.getPosition();
            System.out.println("Position 2 set.");

            // Create the gui.
            GuiEditOptionSelector selector = new GuiEditOptionSelector(this);
            selector.setX(800 / 2);
            selector.setY( 600 / 2);
            Client.getInstance().getGuiTracker().addGui(selector);
            selector.setVisible(true);
        }
    }

    public IntVector2D getPosition1()
    {
        return position1;
    }

    public IntVector2D getPosition2()
    {
        return position2;
    }

    public WorldMap getClipboard()
    {
        return clipboard;
    }

    public void setClipboard(WorldMap clipboard)
    {
        this.clipboard = clipboard;
    }

    public void reset() {
        position1 = null;
        position2 = null;
    }
}
