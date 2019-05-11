package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.animations.AnimateReverseGravityAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.ReverseGravityPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Arrays;
import java.util.List;

public class ReverseGravity extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("ReverseGravity");
    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;
    private static final int DAMAGE = 15;
    private static final int WEAK = 1;

    public ReverseGravity()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);

        baseDamage = DAMAGE;
        isMultiDamage = true;
        magicNumber = baseMagicNumber = WEAK;
        magicNumber2 = baseMagicNumber2 = DAMAGE;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                AttackNote.get(),
                DebuffNote.get(),
                AttackNote.get()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new AnimateReverseGravityAction());

        addToBottom(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            addToBottom(new ApplyPowerAction(mo, p, new WeakPower(mo, magicNumber, false), magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
        addToBottom(new ApplyPowerAction(p, p, new ReverseGravityPower(p, magicNumber2, magicNumber), magicNumber2));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new ReverseGravity();
    }
}
