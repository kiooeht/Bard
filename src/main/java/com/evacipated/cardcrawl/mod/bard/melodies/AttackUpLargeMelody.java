package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

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
    public void play()
    {
        addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 3), 3));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new AttackUpLargeMelody();
    }
}
