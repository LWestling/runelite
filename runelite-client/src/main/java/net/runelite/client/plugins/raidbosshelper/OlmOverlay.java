package net.runelite.client.plugins.raidbosshelper;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LukasW on 2018-05-12.
 */
@Slf4j
public class OlmOverlay extends Overlay {
    private Olm olm;
    private RaidBossHelperPlugin plugin;

    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private SkillIconManager iconManager;

    @Inject
    private ItemManager itemManager;

    private HashMap<Olm.OlmAttack, Skill> iconMap;

    @Inject
    public OlmOverlay(RaidBossHelperConfig config, RaidBossHelperPlugin plugin) {
        this.plugin = plugin;

        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.HIGH);
        setLayer(OverlayLayer.ABOVE_WIDGETS);

        iconMap = new HashMap<>();
        iconMap.put(Olm.OlmAttack.DEFAULT, Skill.MAGIC);
        iconMap.put(Olm.OlmAttack.CRYSTAL, Skill.AGILITY);
        iconMap.put(Olm.OlmAttack.LIGHTNING, Skill.PRAYER);
        iconMap.put(Olm.OlmAttack.SWAP, Skill.HUNTER);
        iconMap.put(Olm.OlmAttack.HEALING, Skill.HITPOINTS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        List<LayoutableRenderableEntity> children = panelComponent.getChildren();

        children.clear();
        children.add(TitleComponent.builder().text("Olm").build());

        // replace with linkedlist where the tail has node to head accessed through array for easy loop
        Olm olm = plugin.getOlm();
        ImageComponent imageComponent;
        for (int index = 0; index < 3; index++) {
            int rotationIndex = olm.getRotationIndex() + index;

            imageComponent = new ImageComponent(
                iconManager.getSkillImage(
                    iconMap.get(
                        olm.getOlmRotation().get(rotationIndex % olm.getOlmRotation().size())
                    )
                )
            );

            imageComponent.setPreferredSize(new Dimension(50 - index * 5, 50 - index * 5));
            children.add(imageComponent);
            log.debug("Index: " + rotationIndex % olm.getOlmRotation().size());
            //graphics.drawImage(imageHashMap.get(olm.getOlmRotation().get(attackIndex)), 5 + index * 10, 5,
             //       25 - index * 5, 25 - index * 5, null);

        }
        panelComponent.render(graphics);
        return null;
    }
}
