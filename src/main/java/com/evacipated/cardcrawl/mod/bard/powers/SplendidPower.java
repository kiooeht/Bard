package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SplendidPower extends TwoAmountPower implements NonStackablePower
{
    public static final String POWER_ID = BardMod.makeID("SplendidForm");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SplendidPower(AbstractCreature owner, int inspiration, int amount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        this.amount = amount;
        this.amount2 = inspiration;
        updateDescription();
        region48 = BardMod.powerAtlas.findRegion("48/splendid");
        region128 = BardMod.powerAtlas.findRegion("128/splendid");
    }

    @Override
    public void updateDescription()
    {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + amount2 + DESCRIPTIONS[3];
        }
    }

    @Override
    public void atStartOfTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new InspirationPower(p, amount, amount2), amount));
    }

    @Override
    public boolean isStackable(AbstractPower power)
    {
        if (power instanceof SplendidPower) {
            if (amount2 == ((SplendidPower) power).amount2) {
                return true;
            }
        }
        return false;
    }
}
