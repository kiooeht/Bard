package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InspireLargeMelody extends AbstractMelody
{
    public InspireLargeMelody()
    {
        super("Inspire (L)", "Gain 2 Inspiration 100.", AbstractCard.CardTarget.SELF);

        notes.add(new AttackNote());
        notes.add(new BuffNote());
        notes.add(new BuffNote());
        notes.add(new BlockNote());
    }

    @Override
    public void play()
    {
        addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new InspirationPower(AbstractDungeon.player, 2, 100), 2));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new InspireLargeMelody();
    }
}
