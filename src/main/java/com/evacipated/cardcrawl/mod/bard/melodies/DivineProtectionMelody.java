package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DivineProtectionMelody extends AbstractMelody
{
    public static final String ID = BardMod.makeID("DivineProtection");

    public DivineProtectionMelody()
    {
        super(ID, AbstractCard.CardTarget.SELF);
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
