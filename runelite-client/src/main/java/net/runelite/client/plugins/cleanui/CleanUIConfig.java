package net.runelite.client.plugins.cleanui;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

/**
 * Created by LukasW on 2018-05-12.
 */
@ConfigGroup(
        keyName = "cleanui",
        name = "Clean UI",
        description = "Clean ui for easy viewing of health and other vital information"
)
public interface CleanUIConfig extends Config {
    @ConfigItem(
            keyName = "offsety",
            name = "Bar Y Offset",
            description = "Offset in Y direction"
    )
    default int getOffsetY() {
        return 102;
    }

    @ConfigItem(
            keyName = "barwidth",
            name = "Bar Width",
            description = "The width of the bars"
    )
    default int getWidth() {
        return 120;
    }

    @ConfigItem(
            keyName = "barheight",
            name = "Bar Height",
            description = "The height of the bars"
    )
    default int getHeight() {
        return 12;
    }

    @ConfigItem(
            keyName = "hideplayerhealth",
            name = "Hide Health bar & Hitsplat",
            description = "Hides your health bar & hit splats on you"
    )
    default boolean isHidingHealthBar() {
        return false;
    }
}
