package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PitchPipe extends AbstractBardRelic
{
    public static final String ID = BardMod.makeID("PitchPipe");

    public PitchPipe()
    {
        super(ID, "test5.png", RelicTier.STARTER, LandingSound.CLINK, Bard.Enums.COLOR);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle()
    {
        if (AbstractDungeon.player instanceof Bard) {
            ((Bard) AbstractDungeon.player).queueNote(new AttackNote());
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new PitchPipe();
    }
}
