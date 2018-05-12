package net.runelite.client.plugins.raidbosshelper;

import lombok.Getter;
import net.runelite.api.NPC;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by LukasW on 2018-05-12.
 * Olm boss
 */
public class Olm implements RaidBoss {
    private static final int OLM_TICKRATE = 6;
    private static final int HEALING_PHASE = 3;
    private static final int ANIMATION_DEFAULT = 1337,
                             ANIMATION_DIRECTION_SWITCH = 1338,
                             ANIMATION_DIRECTION_SWITCH_2 = 1339;

    private static final OlmAttack[] STANDARD_ROTATION =
            {
                OlmAttack.DEFAULT, OlmAttack.DEFAULT,
                OlmAttack.CRYSTAL, OlmAttack.DEFAULT, OlmAttack.DEFAULT,
                OlmAttack.LIGHTNING, OlmAttack.DEFAULT, OlmAttack.DEFAULT,
                OlmAttack.SWAP
            },
            HEALING_ROTATION =
            {
                    OlmAttack.DEFAULT, OlmAttack.DEFAULT,
                    OlmAttack.HEALING
            };


    enum OlmAttack {
        DEFAULT,
        LIGHTNING,
        CRYSTAL,
        SWAP,
        HEALING
    }

    private int phase, tick;

    @Getter
    private int rotationIndex;

    @Getter
    private ArrayList<OlmAttack> olmRotation;

    private NPC olm;

    public Olm(NPC olm, int phase) {
        this.phase = phase;
        this.olm = olm;

        rotationIndex = 0;
        olmRotation = new ArrayList<>();
        buildRotation();
    }

    private void buildRotation() {
        Collections.addAll(olmRotation, STANDARD_ROTATION);
        if (phase == HEALING_PHASE)
            Collections.addAll(olmRotation, HEALING_ROTATION);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    private void onAttack() {
        rotationIndex++;
    }

    @Override
    public void onTick() {
        tick++;
        if (tick % OLM_TICKRATE == 0)
        {
            tick = 0;
            if(!isOlmChangingDirection()) {
                onAttack();
            }
        }
    }

    // a lot of place holders
    private boolean isOlmChangingDirection() {
        return olm.getAnimation() == ANIMATION_DIRECTION_SWITCH || olm.getAnimation() == ANIMATION_DIRECTION_SWITCH_2;
    }
}
