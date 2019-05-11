package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class FirstStrike extends AbstractBardCard implements StartupCard
{
    public static final String ID = BardMod.makeID("FirstStrike");
    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 3;

    public FirstStrike()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);

        tags.add(CardTags.STRIKE);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(AttackNote.get());
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(magicNumber), damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
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
        return new FirstStrike();
    }
}
