package net.runelite.client.plugins.raidbosshelper;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import jdk.nashorn.internal.objects.annotations.Getter;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;

/**
 * Created by LukasW on 2018-05-12.
 * Top class for the raid boss helper
 * will go to another class depending on the
 * boss.
 */
@PluginDescriptor(
        name = "Raid Boss Helper"
)
public class RaidBossHelperPlugin extends Plugin {
    private static final int OLM = 1337;

    @Inject
    private RaidBossHelperOverlay masterOverlay;
    @Inject
    private OlmOverlay olmOverlay;
    @lombok.Getter
    private Olm olm;
    private int currentBoss;

    @Override
    public Overlay getOverlay()
    {
        return masterOverlay;
    }

    @Override
    public void configure(Binder binder)
    {
    }

    protected void startUp() throws Exception
    {
        currentBoss = -1;
    }

    protected void shutDown() throws Exception
    {
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
        if (npcSpawned.getNpc().getName() == null) return;

        if (npcSpawned.getNpc().getName().equalsIgnoreCase("Man"))
        {
            olm = new Olm(npcSpawned.getNpc(), 0);
            masterOverlay.setCurrentOverlay(olmOverlay);
            currentBoss = OLM;
        }
    }


    @Provides
    RaidBossHelperConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(RaidBossHelperConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        if (currentBoss == OLM) {
            olm.onTick();
        }
    }
}
