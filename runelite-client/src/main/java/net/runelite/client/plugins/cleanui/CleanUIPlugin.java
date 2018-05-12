package net.runelite.client.plugins.cleanui;

import com.google.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;

/**
 * Created by LukasW on 2018-05-12.
 */
@PluginDescriptor(
        name="Clean UI"
)
public class CleanUIPlugin extends Plugin {
    @Inject
    private CleanUIOverlay overlay;

    @Override
    public Overlay getOverlay() {
        return overlay;
    }

    public CleanUIPlugin() {

    }
}
