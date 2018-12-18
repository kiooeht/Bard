package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.mod.bard.powers.interfaces.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class InspirationPower extends TwoAmountPower implements NonStackablePower
{
    public static final String POWER_ID = BardMod.makeID("Inspiration");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public InspirationPower(AbstractCreature owner, int cards, int percent)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = cards;
        amount2 = percent;
        priority = 6;
        updateDescription();
        region48 = BardMod.powerAtlas.findRegion("48/inspiration");
        region128 = BardMod.powerAtlas.findRegion("128/inspiration");
    }

    @Override
    public void updateDescription()
    {
        name = NAME + " " + amount2;
        if (amount == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + DESCRIPTIONS[4] + amount2 + DESCRIPTIONS[5];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[3] + DESCRIPTIONS[4] + amount2 + DESCRIPTIONS[5];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (AbstractBardCard.isDamageDealingCard(card) || AbstractBardCard.isBlockGainingCard(card)) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * (1.0f + amount2 / 100.0f);
        }
        return damage;
    }

    @Override
    public float modifyBlock(float blockAmount)
    {
        return blockAmount * (1.0f + amount2 / 100.0f);
    }

    @Override
    public boolean isStackable(AbstractPower power)
    {
        if (power instanceof InspirationPower) {
            if (amount2 == ((InspirationPower) power).amount2) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
    }
}
