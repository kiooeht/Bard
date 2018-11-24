package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;

public class AttackUpLargeMelody extends AbstractMelody
{
    public AttackUpLargeMelody()
    {
        notes.add(new BlockNote());
        notes.add(new AttackNote());
        notes.add(new BuffNote());
        notes.add(new AttackNote());
    }


    @Override
    public AbstractMelody makeCopy()
    {
        return new AttackUpLargeMelody();
    }
}
