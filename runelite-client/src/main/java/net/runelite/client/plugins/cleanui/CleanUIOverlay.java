package net.runelite.client.plugins.cleanui;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by LukasW on 2018-05-12.
 * Clean ui like Wow :p
 */
public class CleanUIOverlay extends Overlay {
    // get from config
    private static final int WIDTH = 80, HEIGHT = 10, OFFSET_Y = 20;
    private Client client;
    private Player player;
    private ArrayList<Bar> bars;

    @Inject
    public CleanUIOverlay(Client client) {
        this.client = client;
        player = client.getLocalPlayer();
        bars = new ArrayList<>();

        bars.add(new Bar(getHPPercentage(), Color.RED, 0));
        bars.add(new Bar(getPrayerPercentage(), Color.RED, 1));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        WorldPoint playerLocation = player.getWorldLocation();

        int x = playerLocation.getX() - WIDTH / 2;
        int y = playerLocation.getY() + OFFSET_Y;

        bars.forEach(bar -> {
            graphics.setColor(bar.getColor());
            graphics.draw3DRect(x, y, WIDTH, HEIGHT, true);
            graphics.fill3DRect(x, y * bar.getGridY() * HEIGHT,
                    (int) (WIDTH * bar.getPercentage()), HEIGHT, true);
        });

        return new Dimension(WIDTH, HEIGHT);
    }

    private float getHPPercentage() {
        return (float) client.getRealSkillLevel(Skill.HITPOINTS) /
                client.getBoostedSkillLevel(Skill.HITPOINTS);
    }

    private float getPrayerPercentage() {
        return (float) client.getRealSkillLevel(Skill.PRAYER) /
                client.getBoostedSkillLevel(Skill.PRAYER);
    }

    private class Bar {
        @Getter
        @Setter
        private float percentage;

        @Getter
        @Setter
        private Color color;

        @Getter
        private int gridY;

        /**
         * A bar on the screen with color and filled percentage
         * @param percentage How filled the bar is in %
         * @param color The color of the filling
         * @param gridY The placement of the bar, 0 is top 1 is below etc
         */
        public Bar(float percentage, Color color, int gridY) {
            this.percentage = percentage;
            this.color = color;
            this.gridY = gridY;
        }
    }
}
