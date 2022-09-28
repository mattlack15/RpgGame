package ca.mattlack.rpg.render;

import ca.mattlack.rpg.entity.Player;
import ca.mattlack.rpg.math.Vector2D;

import java.awt.*;

public class PlayerCamera {
    private final Player player;

    private int viewWidth;
    private int viewHeight;

    public PlayerCamera(Player player, int viewWidth, int viewHeight) {
        this.player = player;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public Player getPlayer() {
        return player;
    }

    public void transform(Graphics2D graphics2D) {
        transform(graphics2D, new Vector2D(0, 0));
    }

    public void transform(Graphics2D graphics2D, Vector2D worldCoordinates) {

        // Translate to the middle of the screen.
        graphics2D.translate(viewWidth / 2, viewHeight / 2);

        // Reflect the camera over the X axis.
        graphics2D.scale(1, -1);

        // Translate to the actual origin.
        graphics2D.translate(-player.getPosition().getX() * GameRenderer.SCALE, -player.getPosition().getY() * GameRenderer.SCALE);

        // Translate to the world coordinates.
        graphics2D.translate(worldCoordinates.getX() * GameRenderer.SCALE, worldCoordinates.getY() * GameRenderer.SCALE);

    }

    public Vector2D screenToWorldCoordinates(Vector2D screenCoordinates) {
        // Translate from the middle of the screen, then divide by the scale.
        Vector2D worldCoordinates = new Vector2D((screenCoordinates.getX() - viewWidth / 2d) / GameRenderer.SCALE,
                -(screenCoordinates.getY() - viewHeight / 2d) / GameRenderer.SCALE);
        worldCoordinates = worldCoordinates.add(player.getPosition()); // Finally, add the player's position.
        return worldCoordinates;
    }

    public Vector2D worldToScreenCoordinates(Vector2D worldCoordinates) {
        // Subtract the player's position and multiply by the scale.
        Vector2D screenCoordinates = new Vector2D((worldCoordinates.getX() - player.getPosition().getX()) * GameRenderer.SCALE,
                -(worldCoordinates.getY() - player.getPosition().getY()) * GameRenderer.SCALE);
        // Move to center of screen.
        screenCoordinates = screenCoordinates.add(viewWidth / 2d, viewHeight / 2d);
        return screenCoordinates;
    }

}
