package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawPower;

public class DrawUpMelody extends AbstractMelody
{
    public DrawUpMelody()
    {
        super("Draw Up", "Draw 1 additional card per turn.", AbstractCard.CardTarget.SELF);

        notes.add(new BuffNote());
        notes.add(new BlockNote());
        notes.add(new BuffNote());
        notes.add(new BuffNote());
    }

    @Override
    public void play()
    {
        addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrawPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new DrawUpMelody();
    }
}
