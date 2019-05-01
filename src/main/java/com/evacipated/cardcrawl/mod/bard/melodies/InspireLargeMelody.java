package com.evacipated.cardcrawl.mod.bard.melodies;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InspireLargeMelody extends AbstractMelody
{
    public static final String ID = BardMod.makeID("InspireLarge");

    public InspireLargeMelody()
    {
        super(ID, AbstractCard.CardTarget.SELF);
    }

    @Override
    protected CustomCard.RegionName getRegionName()
    {
        return new CustomCard.RegionName(BardMod.ID + "/power/splendidForm");
    }

    @Override
    public void play()
    {
        addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new InspirationPower(AbstractDungeon.player, 2, 100), 2));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new InspireLargeMelody();
    }
}
