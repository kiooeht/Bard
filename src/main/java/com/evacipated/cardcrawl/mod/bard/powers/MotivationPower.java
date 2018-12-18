package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MotivationPower extends AbstractPower implements OnReceivePowerPower
{
    public static final String POWER_ID = BardMod.makeID("Motivation");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MotivationPower(AbstractCreature owner, int amount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
        region48 = BardMod.powerAtlas.findRegion("48/motivation");
        region128 = BardMod.powerAtlas.findRegion("128/motivation");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (power instanceof InspirationPower) {
            ((InspirationPower) power).amount2 += amount;
            power.updateDescription();
        }

        return true;
    }
}