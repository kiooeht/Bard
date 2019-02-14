package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Arrays;
import java.util.List;

public class ViciousMockery extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("ViciousMockery");
    public static final String IMG = null;
    private static final int COST = 0;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int WEAK = 1;

    public ViciousMockery()
    {
        super(ID, IMG, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.ENEMY);

        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = WEAK;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                new DebuffNote(),
                new AttackNote()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new ViciousMockery();
    }
}
