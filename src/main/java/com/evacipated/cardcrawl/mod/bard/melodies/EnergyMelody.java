package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class EnergyMelody extends AbstractMelody
{
    public EnergyMelody()
    {
        super("Energy", "Gain [E] [E].", AbstractCard.CardTarget.SELF);

        notes.add(new BuffNote());
        notes.add(new BuffNote());
        notes.add(new AttackNote());
        notes.add(new BlockNote());
    }

    @Override
    public void play()
    {
        addToBottom(new GainEnergyAction(2));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new EnergyMelody();
    }
}
