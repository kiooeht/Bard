package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DamageSmallMelody extends AbstractMelody
{
    public DamageSmallMelody()
    {
        super("Damage (S)", "Deal 10 damage to ALL enemies.", AbstractCard.CardTarget.ALL_ENEMY);

        notes.add(new AttackNote());
        notes.add(new DebuffNote());
        notes.add(new AttackNote());
    }

    @Override
    public void play()
    {
        addToBottom(new DamageAllEnemiesAction(
                AbstractDungeon.player,
                DamageInfo.createDamageMatrix(10, true),
                DamageInfo.DamageType.THORNS,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        ));
    }

    @Override
    public AbstractCard makeChoiceCard()
    {
        AbstractCard ret = super.makeChoiceCard();
        ret.type = AbstractCard.CardType.ATTACK;
        return ret;
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new DamageSmallMelody();
    }
}
