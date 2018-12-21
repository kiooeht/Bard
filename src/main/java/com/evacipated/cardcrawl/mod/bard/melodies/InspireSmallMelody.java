package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InspireSmallMelody extends AbstractMelody
{
    public InspireSmallMelody()
    {
        super("Inspire (S)", "Gain 2 Inspiration 50.", AbstractCard.CardTarget.SELF);

        notes.add(new AttackNote());
        notes.add(new BuffNote());
        notes.add(new BlockNote());
    }

    @Override
    public void play()
    {
        addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new InspirationPower(AbstractDungeon.player, 2, 50), 2));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new InspireSmallMelody();
    }
}
