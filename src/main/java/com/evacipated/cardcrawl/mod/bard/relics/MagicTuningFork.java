package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.WildCardNote;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MagicTuningFork extends AbstractBardRelic
{
    public static final String ID = BardMod.makeID("MagicTuningFork");

    public MagicTuningFork()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL, Bard.Enums.COLOR);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart()
    {
        addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBottom(new QueueNoteAction(WildCardNote.get()));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new MagicTuningFork();
    }
}
