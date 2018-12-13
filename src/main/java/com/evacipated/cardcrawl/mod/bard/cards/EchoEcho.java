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
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class EchoEcho extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("EchoEcho");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int INITIAL_DAMAGE_BONUS = 4;

    public EchoEcho()
    {
        super(ID, IMG, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.ENEMY);

        baseDamage = DAMAGE;
        magicNumber2 = baseMagicNumber2 = DAMAGE + INITIAL_DAMAGE_BONUS;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new AttackNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new DamageAction(m, new DamageInfo(p, magicNumber2, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void applyPowers()
    {
        magicNumber2 = baseMagicNumber2;

        int tmp = baseDamage;
        baseDamage = baseMagicNumber2;

        super.applyPowers();

        magicNumber2 = damage;
        baseDamage = tmp;

        super.applyPowers();

        isMagicNumber2Modified = (magicNumber2 != baseMagicNumber2);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        magicNumber2 = baseMagicNumber2;

        int tmp = baseDamage;
        baseDamage = baseMagicNumber2;

        super.calculateCardDamage(mo);

        magicNumber2 = damage;
        baseDamage = tmp;

        super.calculateCardDamage(mo);

        isMagicNumber2Modified = (magicNumber2 != baseMagicNumber2);
    }

    @Override
    public void onMoveToDiscard()
    {
        magicNumber2 = baseMagicNumber2;
        isMagicNumber2Modified = false;
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber2(UPGRADE_DAMAGE);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new EchoEcho();
    }
}
