package com.evacipated.cardcrawl.mod.bard.melodies;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class WeakenSmallMelody extends AbstractMelody
{
    public static final String ID = BardMod.makeID("WeakenSmall");

    public WeakenSmallMelody()
    {
        super(ID, AbstractCard.CardTarget.ALL_ENEMY);
        type = AbstractCard.CardType.SKILL;
    }

    @Override
    protected CustomCard.RegionName getRegionName()
    {
        return new CustomCard.RegionName("colorless/skill/blind");
    }

    @Override
    public void play()
    {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, 1, false), 1, true));
        }
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new WeakenSmallMelody();
    }
}
