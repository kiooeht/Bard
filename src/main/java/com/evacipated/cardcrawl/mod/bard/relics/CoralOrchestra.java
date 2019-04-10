package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.hooks.OnMelodyPlayedHook;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

public class CoralOrchestra extends AbstractBardRelic implements OnMelodyPlayedHook
{
    public static final String ID = BardMod.makeID("CoralOrchestra");
    private static final int MELODY_COUNT = 5;

    public CoralOrchestra()
    {
        super(ID, RelicTier.RARE, LandingSound.HEAVY, Bard.Enums.COLOR);

        setCounter(0);
    }

    @Override
    public String getUpdatedDescription()
    {
        if (Settings.usesOrdinal) {
            return DESCRIPTIONS[0] + MELODY_COUNT + TopPanel.getOrdinalNaming(MELODY_COUNT) + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0] + MELODY_COUNT + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onMelodyPlayed(AbstractMelody melody)
    {
        setCounter(counter + 1);
        if (counter == MELODY_COUNT) {
            setCounter(0);
            flash();
            pulse = false;
            melody.play();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        } else if (counter == MELODY_COUNT - 1) {
            beginPulse();
            pulse = true;
        }
    }

    @Override
    public void atBattleStart()
    {
        if (counter == MELODY_COUNT - 1) {
            beginPulse();
            pulse = true;
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new CoralOrchestra();
    }
}
