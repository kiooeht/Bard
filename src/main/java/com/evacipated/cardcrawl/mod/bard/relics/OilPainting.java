package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OilPainting extends AbstractBardRelic
{
    public static final String ID = BardMod.makeID("OilPainting");
    private static final int INSPIRATION_AMT = 100;

    public OilPainting()
    {
        super(ID, RelicTier.COMMON, LandingSound.FLAT, Bard.Enums.COLOR);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + INSPIRATION_AMT + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart()
    {
        flash();
        addToTop(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new InspirationPower(AbstractDungeon.player, 1, INSPIRATION_AMT),
                1
        ));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new OilPainting();
    }
}
