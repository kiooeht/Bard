package com.evacipated.cardcrawl.mod.bard.melodies;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class DefenseUpSmallMelody extends AbstractMelody
{
    public static final String ID = BardMod.makeID("DefenseUpSmall");

    public DefenseUpSmallMelody()
    {
        super(ID, AbstractCard.CardTarget.SELF);
    }

    @Override
    protected CustomCard.RegionName getRegionName()
    {
        return new CustomCard.RegionName("green/power/footwork");
    }

    @Override
    public void play()
    {
        addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new DefenseUpSmallMelody();
    }
}
