package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SilencedBuffsPower extends AbstractBardPower implements OnReceivePowerPower
{
    public static final String POWER_ID = BardMod.makeID("SilencedBuffs");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean justApplied = false;

    public SilencedBuffsPower(AbstractCreature owner, int turns)
    {
        this(owner, turns, false);
    }

    public SilencedBuffsPower(AbstractCreature owner, int turns, boolean isSourceMonster)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = turns;
        if (isSourceMonster) {
            justApplied = true;
        }
        type = PowerType.DEBUFF;
        isTurnBased = true;
        updateDescription();
        loadRegion("silenced");
    }

    @Override
    public void updateDescription()
    {
        if (owner == null || owner.isPlayer) {
            description = DESCRIPTIONS[0];
        } else {
            description = FontHelper.colorString(owner.name, "y") + DESCRIPTIONS[1];
        }

        description += amount;

        if (amount == 1) {
            description += DESCRIPTIONS[3];
        } else {
            description += DESCRIPTIONS[2];
        }
    }

    @Override
    public void atEndOfRound()
    {
        if (justApplied) {
            justApplied = false;
            return;
        }

        if (amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (power.type == PowerType.BUFF && source == owner) {
            flash();
            return false;
        }
        return true;
    }
}
