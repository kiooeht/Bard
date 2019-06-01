package com.evacipated.cardcrawl.mod.bard.relics;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.hooks.OnMelodyPlayedHook;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EchoChamber extends AbstractBardRelic implements OnMelodyPlayedHook, CustomSavable<String>
{
    public static final String ID = BardMod.makeID("EchoChamber");

    private AbstractMelody lastMelody = null;

    public EchoChamber()
    {
        super(ID, RelicTier.RARE, LandingSound.HEAVY, Bard.Enums.COLOR);
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c)
    {
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription()
    {
        String append = "";
        if (lastMelody != null) {
            append = DESCRIPTIONS[1] + FontHelper.colorString(lastMelody.getName(), "b") + DESCRIPTIONS[2];
        }
        return DESCRIPTIONS[0] + append;
    }

    @Override
    public String onSave()
    {
        if (lastMelody == null) {
            return null;
        }
        return lastMelody.getID();
    }

    @Override
    public void onLoad(String melodyID)
    {
        if (melodyID == null) {
            lastMelody = null;
            return;
        }

        lastMelody = MelodyManager.getMelodByID(melodyID);
        onMelodyPlayed(lastMelody);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        if (lastMelody != null) {
            stopPulse();
            flash();
            addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            lastMelody.play();
            lastMelody = null;
            updateDescription(AbstractDungeon.player.chosenClass);
        }
    }

    @Override
    public void onMelodyPlayed(AbstractMelody melody)
    {
        flash();
        beginLongPulse();
        lastMelody = melody;
        updateDescription(AbstractDungeon.player.chosenClass);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new EchoChamber();
    }
}
