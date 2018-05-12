package net.runelite.client.plugins.raidbosshelper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

/**
 * Created by LukasW on 2018-05-12.
 */

@ConfigGroup(
        keyName = "raidbosshelper",
        name = "Raid Boss Helper",
        description = "Tips and notifications when fighting bosses in raids."
)
public interface RaidBossHelperConfig extends Config {
    @ConfigItem(
            keyName = "showolmrotation",
            name = "Show Olm Rotation",
            description = "Shows olm rotation to indicate what he will do next"
    )
    default boolean showOlmRotation() { return true; }
}
