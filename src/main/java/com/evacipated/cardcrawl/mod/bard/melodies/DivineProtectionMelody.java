package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DivineProtectionMelody extends AbstractMelody
{
    public DivineProtectionMelody()
    {
        super("Divine Protection", "Gain 10 Temporary_HP.", AbstractCard.CardTarget.SELF);

        notes.add(new BlockNote());
        notes.add(new BuffNote());
        notes.add(new BlockNote());
    }

    @Override
    public void play()
    {
        addToBottom(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, 10));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new DivineProtectionMelody();
    }
}
