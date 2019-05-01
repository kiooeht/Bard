package com.evacipated.cardcrawl.mod.bard.melodies;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DrawMelody extends AbstractMelody
{
    public static final String ID = BardMod.makeID("Draw");

    public DrawMelody()
    {
        super(ID, AbstractCard.CardTarget.SELF);
        type = AbstractCard.CardType.SKILL;
    }

    @Override
    protected CustomCard.RegionName getRegionName()
    {
        return new CustomCard.RegionName("blue/skill/skim");
    }

    @Override
    public void play()
    {
        addToBottom(new DrawCardAction(AbstractDungeon.player, 3));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new DrawMelody();
    }
}
