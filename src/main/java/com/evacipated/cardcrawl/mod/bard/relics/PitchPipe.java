package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PitchPipe extends AbstractBardRelic
{
    public static final String ID = BardMod.makeID("PitchPipe");

    public PitchPipe()
    {
        super(ID, RelicTier.STARTER, LandingSound.CLINK, Bard.Enums.COLOR);
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
            addToBottom(new QueueNoteAction(BuffNote.get()));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new PitchPipe();
    }
}
