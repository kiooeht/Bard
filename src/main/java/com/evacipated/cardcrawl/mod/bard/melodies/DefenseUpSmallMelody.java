package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class DefenseUpSmallMelody extends AbstractMelody
{
    public DefenseUpSmallMelody()
    {
        super("Defense Up (S)", "Gain 1 Dexterity.", AbstractCard.CardTarget.SELF);

        notes.add(new BuffNote());
        notes.add(new BlockNote());
        notes.add(new BlockNote());
    }

    @Override
    public void play()
    {
        addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new DefenseUpSmallMelody();
    }
}
