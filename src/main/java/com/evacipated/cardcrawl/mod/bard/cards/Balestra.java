package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Balestra extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Balestra");
    public static final String IMG = null;
    private static final int COST = 2;
    private static final int DAMAGE = 5;
    private static final int TIMES = 3;
    private static final int UPGRADE_TIMES = 1;

    public Balestra()
    {
        super(ID, IMG, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);

        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = TIMES;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new AttackNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i=0; i<magicNumber; ++i) {
            addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), randomEffect()));
        }
    }

    private AbstractGameAction.AttackEffect randomEffect()
    {
        switch (new Random().nextInt(3)) {
            case 0:
                return AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
            case 1:
                return AbstractGameAction.AttackEffect.SLASH_VERTICAL;
            case 2:
                return AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        }
        // Should never happen
        return AbstractGameAction.AttackEffect.BLUNT_LIGHT;
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
            AbstractPower strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
            if (strength != null) {
                damage -= strength.amount;
            }
        }

        AbstractPower dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
        if (dexterity != null) {
            damage += dexterity.amount;
        }

        isDamageModified = damage != baseDamage;
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_TIMES);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Balestra();
    }
}
