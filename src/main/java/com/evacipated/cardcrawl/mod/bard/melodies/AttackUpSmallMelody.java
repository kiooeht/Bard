package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;

public class AttackUpSmallMelody extends AbstractMelody
{
    public AttackUpSmallMelody()
    {
        notes.add(new AttackNote());
        notes.add(new AttackNote());
    }


    @Override
    public AbstractMelody makeCopy()
    {
        return new AttackUpSmallMelody();
    }
}
