package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.animations.AnimateReverseGravityAction;
import com.evacipated.cardcrawl.mod.bard.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.mod.bard.powers.interfaces.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class ReverseGravityPower extends TwoAmountPower implements NonStackablePower
{
    public static final String POWER_ID = BardMod.makeID("ReverseGravity");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ReverseGravityPower(AbstractCreature owner, int damage, int weak)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount2 = damage;
        amount = weak;
        isTurnBased = true;
        updateDescription();
        // TODO
        loadRegion("rupture");
    }

    @Override
    public void updateDescription()
    {
        description = String.format(DESCRIPTIONS[0], amount2, amount);
    }

    @Override
    public void atStartOfTurn()
    {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));

            AbstractDungeon.actionManager.addToBottom(new AnimateReverseGravityAction());

            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(amount2, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, owner, new WeakPower(mo, amount, false), amount, true, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }
}
