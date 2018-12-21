package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.powers.interfaces.ModifyInspirationPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MantleOfInspirationPower extends AbstractPower implements ModifyInspirationPower
{
    public static final String POWER_ID = BardMod.makeID("MantleOfInspiration");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MantleOfInspirationPower(AbstractCreature owner, int amount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
        region48 = BardMod.powerAtlas.findRegion("48/mantleOfInspiration");
        region128 = BardMod.powerAtlas.findRegion("128/mantleOfInspiration");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public float modifyInspiration(float inspirationAmount)
    {
        return inspirationAmount + amount;
    }
}