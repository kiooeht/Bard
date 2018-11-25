package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AttackUpSmallMelody extends AbstractMelody
{
    public AttackUpSmallMelody()
    {
        notes.add(new AttackNote());
        notes.add(new AttackNote());
    }

    @Override
    public void play()
    {
        addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new AttackUpSmallMelody();
    }
}
