package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.IncreaseMaxNotesAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ConductorsBaton extends AbstractBardRelic
{
    public static final String ID = BardMod.makeID("ConductorsBaton");
    private static final int EXTRA_NOTES = 2;

    public ConductorsBaton()
    {
        super(ID, "test5.png", RelicTier.SHOP, LandingSound.FLAT, Bard.Enums.COLOR);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + EXTRA_NOTES + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart()
    {
        addToTop(new IncreaseMaxNotesAction(EXTRA_NOTES));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new ConductorsBaton();
    }
}
