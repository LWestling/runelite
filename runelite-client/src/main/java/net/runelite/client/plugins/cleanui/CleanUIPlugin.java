package net.runelite.client.plugins.cleanui;

import ch.qos.logback.core.ConsoleAppender;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;

import java.util.logging.ConsoleHandler;

/**
 * Created by LukasW on 2018-05-12.
 */
@PluginDescriptor(
        name="Clean UI",
        enabledByDefault = false
)
public class CleanUIPlugin extends Plugin {
    @Inject
    private CleanUIOverlay cleanUIOverlay;

    @Inject
    private CleanUIConfig cleanUIConfig;

    @Inject
    private Client client;

    @Inject
    private SpriteManager spriteManager;

    @Override
    public Overlay getOverlay() {
        return cleanUIOverlay;
    }

    private int healthCashed = 0;

    @Provides
    CleanUIConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(CleanUIConfig.class);
    }

    @Override
    protected void startUp() {
        // to activate them on startup
        updateConfigs();
        healthCashed = client.getBoostedSkillLevel(Skill.HITPOINTS);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged e)
    {
        updateConfigs();
    }

    private void updateConfigs() {

        client.setIsHidingEntities(true);
        client.setLocalPlayerHidden2D(cleanUIConfig.isHidingHealthBar());
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        calculateHealthDifference();
        setOverheadIcon();

        int poisonValue = client.getVarps()[102];
        if (poisonValue > 0) {
            if (poisonValue < 1000000) {
                cleanUIOverlay.setHealthCondition(CleanUIOverlay.HealthCondition.POISON);
            } else {
                cleanUIOverlay.setHealthCondition(CleanUIOverlay.HealthCondition.VENOM);
            }
        } else {
            cleanUIOverlay.setHealthCondition(CleanUIOverlay.HealthCondition.NONE);
        }
    }

    private void setOverheadIcon() {
        HeadIcon icon = client.getLocalPlayer().getOverheadIcon();
        if (icon == null) {
            cleanUIOverlay.setOverhead(null);
            return;
        }

        switch (icon) {
            case RETRIBUTION:
                cleanUIOverlay.setOverhead(spriteManager.getSprite(SpriteID.PRAYER_RETRIBUTION, 0));
                break;
            case REDEMPTION:
                cleanUIOverlay.setOverhead(spriteManager.getSprite(SpriteID.PRAYER_REDEMPTION, 0));
                break;
            case RANGED:
                cleanUIOverlay.setOverhead(spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MISSILES, 0));
                break;
            case SMITE:
                cleanUIOverlay.setOverhead(spriteManager.getSprite(SpriteID.PRAYER_SMITE, 0));
                break;
            case MELEE:
                cleanUIOverlay.setOverhead(spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MELEE, 0));
                break;
            case MAGIC:
                cleanUIOverlay.setOverhead(spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MAGIC, 0));
                break;
            default: cleanUIOverlay.setOverhead(null);
        }
    }

    public void calculateHealthDifference() {
        int newHealth = client.getBoostedSkillLevel(Skill.HITPOINTS);
        int diff = newHealth - healthCashed;
        healthCashed = newHealth;

        if (diff != 0) cleanUIOverlay.addFloatingNumber(new CleanUIOverlay.FloatingNumber(diff, 0, 200));
    }
}
