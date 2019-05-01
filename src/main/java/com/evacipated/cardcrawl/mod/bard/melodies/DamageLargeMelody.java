package com.evacipated.cardcrawl.mod.bard.melodies;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DamageLargeMelody extends AbstractMelody
{
    public static final String ID = BardMod.makeID("DamageLarge");

    public DamageLargeMelody()
    {
        super(ID, AbstractCard.CardTarget.ALL_ENEMY);
        type = AbstractCard.CardType.ATTACK;
    }

    @Override
    protected CustomCard.RegionName getRegionName()
    {
        return new CustomCard.RegionName("red/attack/whirlwind");
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
    public AbstractMelody makeCopy()
    {
        return new DamageLargeMelody();
    }
}
