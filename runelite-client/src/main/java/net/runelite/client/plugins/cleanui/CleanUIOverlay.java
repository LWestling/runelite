package net.runelite.client.plugins.cleanui;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import static net.runelite.client.plugins.cleanui.CleanUIOverlay.HealthCondition.NONE;

/**
 * Created by LukasW on 2018-05-12.
 * Clean ui like Wow :p
 */
public class CleanUIOverlay extends Overlay {
    private final static Color VENOMED = new Color(20, 100, 10);

    private Client client;
    private CleanUIConfig config;
    private ArrayList<Bar> bars;

    @Setter
    private BufferedImage overhead;

    // if u can get onHitSplat or something i could do tha instead
    private LinkedList<FloatingNumber> healthDifferences;

    enum HealthCondition {
        NONE, POISON, VENOM
    };
    private HealthCondition condition;

    @Inject
    public CleanUIOverlay(Client client, CleanUIConfig config) {
        this.client = client;
        this.config = config;

        bars = new ArrayList<>();
        healthDifferences = new LinkedList<>();

        bars.add(new Bar(getHPPercentage(), Color.RED, 0));
        bars.add(new Bar(getPrayerPercentage(), Color.CYAN, 1));
        bars.add(new Bar(getPrayerPercentage(), Color.YELLOW, 2));

        condition = NONE;
        overhead = null;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Player player = client.getLocalPlayer();
        if (player == null) return null;

        // todo fix
        bars.get(0).setPercentage(getHPPercentage());
        bars.get(1).setPercentage(getPrayerPercentage());
        bars.get(2).setPercentage(getEnergyPercentage());

        LocalPoint playerLocation = player.getLocalLocation();
        Point point = Perspective.worldToCanvas(client, playerLocation.getX(), playerLocation.getY(), client.getPlane());

        int x = point.getX() - config.getWidth() / 2;
        int y = point.getY() + config.getOffsetY() - 125;

        bars.forEach(bar -> {
            float percentage = bar.getPercentage();
            if (percentage > 1) {
                percentage = 1;
                graphics.setColor(Color.GREEN);
            } else {
                graphics.setColor(Color.BLACK);
            }

            graphics.draw3DRect(x, y + bar.getGridY() * config.getHeight() + 3,
                    config.getWidth(), config.getHeight(), true);
            graphics.fill3DRect(x, y + bar.getGridY() * config.getHeight() + 3,
                    config.getWidth(), config.getHeight(), true);
            graphics.setColor(bar.getColor());

            if (percentage > 0)
                graphics.fill3DRect(x + 1, y + bar.getGridY() * config.getHeight() + 4,
                        (int) (config.getWidth() * percentage) - 1, config.getHeight() - 2, true);
        });

        // this is just a test
        healthDifferences.forEach(floatingNumber -> {
            String hit;
            if (floatingNumber.getNumber() > 0) {
                graphics.setColor(Color.GREEN);
                hit = "+ " + floatingNumber.getNumber();
            }
            else
            {
                graphics.setColor(Color.RED);
                hit = String.valueOf(floatingNumber.getNumber());
            }
            graphics.setFont(new Font("TimesRoman", Font.BOLD, Math.abs(floatingNumber.getNumber() / 8) + 20));
            graphics.drawString(hit, x + config.getWidth() + 10, y + config.getHeight() / 2 - floatingNumber.getY());
            floatingNumber.update(2);
        });

        if (overhead != null) {
            graphics.drawImage(
                    overhead,x - 30, y + config.getHeight() * bars.size() / 2 - 8,
                    null
            );
        }

        healthDifferences.removeIf(floatingNumber -> floatingNumber.getY() == floatingNumber.getMaxY());

        return null;
    }

    void setHealthCondition(HealthCondition condition) {
        this.condition = condition;
        switch (condition) {
            case NONE:
                bars.get(0).setColor(Color.RED);
                break;
            case VENOM:
                bars.get(0).setColor(VENOMED);
                break;
            case POISON:
                bars.get(0).setColor(Color.GREEN);
                break;
        }
    }

    void addFloatingNumber(FloatingNumber floatingNumber) {
        healthDifferences.add(floatingNumber);
    }

    private float getHPPercentage() {
        return (float) client.getBoostedSkillLevel(Skill.HITPOINTS) /
                client.getRealSkillLevel(Skill.HITPOINTS);
    }

    private float getPrayerPercentage() {
        return (float) client.getBoostedSkillLevel(Skill.PRAYER) /
                client.getRealSkillLevel(Skill.PRAYER);
    }

    private float getEnergyPercentage() {
        return (float) client.getEnergy() / 100;
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

    static class FloatingNumber {
        @Setter
        @Getter
        private int number;

        @Getter
        @Setter
        private int y;

        @Getter
        @Setter
        private int maxY;

        FloatingNumber(int number, int y, int maxY) {
            this.number = number;
            this.y = y;
            this.maxY = maxY;
        }

        public void update(int addToY) {
            y += addToY;
            if (y >= maxY) y = maxY;
        }
    }
}
