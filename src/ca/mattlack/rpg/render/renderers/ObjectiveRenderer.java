package ca.mattlack.rpg.render.renderers;

import ca.mattlack.rpg.Client;
import ca.mattlack.rpg.math.Vector2D;
import ca.mattlack.rpg.quest.GameQuest;
import ca.mattlack.rpg.quest.Objective;
import ca.mattlack.rpg.render.PlayerCamera;
import ca.mattlack.rpg.render.Renderer;
import ca.mattlack.rpg.world.Location;

import java.awt.*;
import java.util.Vector;

public class ObjectiveRenderer extends Renderer
{

    public ObjectiveRenderer()
    {
        super("objective");
    }

    @Override
    public void render(Graphics2D screen, PlayerCamera camera)
    {
        GameQuest quest = camera.getPlayer().getQuest();
        if (quest == null) return;
        Objective objective = quest.getCurrentObjective();
        if (objective == null) return;
        String description = objective.getDescription();

        int posX = 25;
        int posY = 50;

        Font font = new Font("Arial", Font.PLAIN, 18);

        screen.setFont(font);

        int padding = 10;
        int boxWidth = screen.getFontMetrics().stringWidth(description) + padding * 2;
        int boxHeight = screen.getFontMetrics().getHeight() * 2 + padding * 2;

        font = new Font("Arial", Font.BOLD, 20);

        screen.setFont(font);

        screen.setColor(new Color(0x654321).darker());
        screen.fillRect(posX, posY, boxWidth, boxHeight);

        screen.setColor(Color.ORANGE);
        screen.drawString("Objective", posX + padding, posY + padding + screen.getFontMetrics().getAscent());

        screen.setColor(Color.WHITE);
        screen.setFont(new Font("Arial", Font.PLAIN, 18));
        screen.drawString(description, posX + padding, posY + padding + screen.getFontMetrics().getAscent() * 2 + 10);

        // White-ish border.
        screen.setColor(Color.WHITE.darker());
        screen.setStroke(new BasicStroke(2));
        screen.drawRect(posX, posY, boxWidth, boxHeight);

        // Arrow pointing to objective.
        Location target = objective.getCurrentTarget();
        if (target != null && camera.getPlayer().getWorld() == target.getWorld()) {
            Vector2D screenPosition = camera.worldToScreenCoordinates(target.getPosition());

            Vector2D arrowHead = new Vector2D(25, 0);
            Vector2D arrowButt = new Vector2D(8, 0);
            Vector2D arrowSide1 = new Vector2D(0, 8);
            Vector2D arrowSide2 = new Vector2D(0, -8);

            if (screenPosition.getX() < 0 || screenPosition.getY() < 0 || screenPosition.getX() > camera.getViewWidth() || screenPosition.getY() > camera.getViewHeight())
            {
                Vector2D direction = screenPosition.subtract(new Vector2D(camera.getViewWidth() / 2d, camera.getViewHeight() / 2d));
                direction.normalize();

                double yaw = Math.atan2(direction.getY(), direction.getX());

                arrowHead = arrowHead.rotate(yaw);
                arrowButt = arrowButt.rotate(yaw);
                arrowSide1 = arrowSide1.rotate(yaw);
                arrowSide2 = arrowSide2.rotate(yaw);

                // Now figure out how to move the head of the arrow to a clamped position of the target.
                Vector2D clampedPosition = new Vector2D(screenPosition.getX(), screenPosition.getY());
                if (clampedPosition.getX() < 12) clampedPosition = clampedPosition.setX(12);
                if (clampedPosition.getX() > camera.getViewWidth()-18) clampedPosition = clampedPosition.setX(camera.getViewWidth()-18);
                if (clampedPosition.getY() < 35) clampedPosition = clampedPosition.setY(35);
                if (clampedPosition.getY() > camera.getViewHeight()-18) clampedPosition = clampedPosition.setY(camera.getViewHeight()-18);

                arrowHead = arrowHead.add(new Vector2D(camera.getViewWidth() / 2d, camera.getViewHeight() / 2d));
                arrowButt = arrowButt.add(new Vector2D(camera.getViewWidth() / 2d, camera.getViewHeight() / 2d));
                arrowSide1 = arrowSide1.add(new Vector2D(camera.getViewWidth() / 2d, camera.getViewHeight() / 2d));
                arrowSide2 = arrowSide2.add(new Vector2D(camera.getViewWidth() / 2d, camera.getViewHeight() / 2d));

                Vector2D diff = clampedPosition.subtract(arrowHead);
                arrowHead = arrowHead.add(diff);
                arrowButt = arrowButt.add(diff);
                arrowSide1 = arrowSide1.add(diff);
                arrowSide2 = arrowSide2.add(diff);

                screen.setStroke(new BasicStroke(3));
                Polygon arrow = new Polygon();
                arrow.addPoint((int) arrowHead.getX(), (int) arrowHead.getY());
                arrow.addPoint((int) arrowSide1.getX(), (int) arrowSide1.getY());
                arrow.addPoint((int) arrowButt.getX(), (int) arrowButt.getY());
                arrow.addPoint((int) arrowSide2.getX(), (int) arrowSide2.getY());

                screen.setColor(Color.WHITE);
                screen.fillPolygon(arrow);
                screen.setColor(new Color(0xFFA500));
                screen.drawPolygon(arrow);

            } else {
                // Arrow should face straight down.
                double yaw = Math.PI / 2d;

                arrowHead = arrowHead.rotate(yaw);
                arrowButt = arrowButt.rotate(yaw);
                arrowSide1 = arrowSide1.rotate(yaw);
                arrowSide2 = arrowSide2.rotate(yaw);

                // Arrow should be slightly on top of the screen coordinates.
                Vector2D diff = screenPosition.add(0, -45 + Math.sin(Client.getInstance().getTick() * Math.PI / 16) * 5).subtract(arrowHead);
                arrowHead = arrowHead.add(diff);
                arrowButt = arrowButt.add(diff);
                arrowSide1 = arrowSide1.add(diff);
                arrowSide2 = arrowSide2.add(diff);

                screen.setStroke(new BasicStroke(3));
                Polygon arrow = new Polygon();
                arrow.addPoint((int) arrowHead.getX(), (int) arrowHead.getY());
                arrow.addPoint((int) arrowSide1.getX(), (int) arrowSide1.getY());
                arrow.addPoint((int) arrowButt.getX(), (int) arrowButt.getY());
                arrow.addPoint((int) arrowSide2.getX(), (int) arrowSide2.getY());

                screen.setColor(Color.WHITE);
                screen.fillPolygon(arrow);
                screen.setColor(new Color(0xFFA500));
                screen.drawPolygon(arrow);
            }
        }
    }

    @Override
    public int priority()
    {
        return 98;
    }
}
