package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FalseLife extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("FalseLife");
    public static final String IMG = null;
    private static final int COST = 1;

    public FalseLife()
    {
        super(ID, IMG, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.ENEMY);
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        if (!upgraded) {
            return Collections.singletonList(new AttackNote());
        }
        return Arrays.asList(new AttackNote(), new AttackNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!upgraded) {
            addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SHIELD));
        } else {
            addToBottom(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SHIELD));
        }
    }

    private void setBaseDamage()
    {
        damage = baseDamage = 2 * TempHPField.tempHp.get(AbstractDungeon.player);
    }

    @Override
    public void applyPowers()
    {
        setBaseDamage();
        super.applyPowers();

        rawDescription = (upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION) + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        setBaseDamage();
        super.calculateCardDamage(mo);

        rawDescription = (upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION) + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard()
    {
        rawDescription = (upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION);
        initializeDescription();
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            target = CardTarget.ALL_ENEMY;
            isMultiDamage = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new FalseLife();
    }
}
