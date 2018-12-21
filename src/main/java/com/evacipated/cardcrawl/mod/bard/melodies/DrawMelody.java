package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DrawMelody extends AbstractMelody
{
    public DrawMelody()
    {
        super("Draw", "Draw 3 cards.", AbstractCard.CardTarget.SELF);

        notes.add(new BuffNote());
        notes.add(new BuffNote());
        notes.add(new BuffNote());
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
