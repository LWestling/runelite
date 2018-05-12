package net.runelite.client.plugins.raidbosshelper;

import net.runelite.api.events.GameTick;

/**
 * Created by LukasW on 2018-05-12.
 * Abstract raid boss class
 */
public interface RaidBoss {
    void onSpawn();
    void onDespawn();
    void onTick();
}
