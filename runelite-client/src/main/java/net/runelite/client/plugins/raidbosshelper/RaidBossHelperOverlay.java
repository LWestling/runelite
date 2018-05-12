package net.runelite.client.plugins.raidbosshelper;

import com.google.inject.Inject;
import lombok.Setter;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;
import java.util.List;

/**
 * Created by LukasW on 2018-05-12.
 */
public class RaidBossHelperOverlay extends Overlay {
    private final PanelComponent panelComponent = new PanelComponent();
    @Setter
    private Overlay currentOverlay;

    @Inject
    public RaidBossHelperOverlay(RaidBossHelperConfig config, RaidBossHelperPlugin plugin) {
        setPosition(OverlayPosition.TOP_LEFT);
        currentOverlay = null;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (currentOverlay != null)
        {
            return currentOverlay.render(graphics);
        }
        List<LayoutableRenderableEntity> children = panelComponent.getChildren();

        children.clear();
        children.add(TitleComponent.builder().text("No Boss").build());
        panelComponent.render(graphics);

        return null;
    }
}
