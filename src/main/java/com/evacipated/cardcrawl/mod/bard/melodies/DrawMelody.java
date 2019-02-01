package com.evacipated.cardcrawl.mod.bard.melodies;

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
