package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DamageLargeMelody extends AbstractMelody
{
    public DamageLargeMelody()
    {
        super("Damage (L)", "Deal 18 damage to ALL enemies.", AbstractCard.CardTarget.ALL_ENEMY);

        notes.add(new AttackNote());
        notes.add(new DebuffNote());
        notes.add(new BuffNote());
        notes.add(new AttackNote());
    }

    @Override
    public void play()
    {
        addToBottom(new DamageAllEnemiesAction(
                AbstractDungeon.player,
                DamageInfo.createDamageMatrix(18, true),
                DamageInfo.DamageType.THORNS,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
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
        return new DamageLargeMelody();
    }
}
