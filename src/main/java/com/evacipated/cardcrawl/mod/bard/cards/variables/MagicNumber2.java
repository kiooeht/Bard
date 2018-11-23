package com.evacipated.cardcrawl.mod.bard.cards.variables;

import basemod.abstracts.DynamicVariable;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class MagicNumber2 extends DynamicVariable
{
    @Override
    public String key()
    {
        return "bard:M2";
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).baseMagicNumber2;
        }
        return -1;
    }

    @Override
    public int value(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).magicNumber2;
        }
        return -1;
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).isMagicNumber2Modified;
        }
        return false;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).upgradedMagicNumber2;
        }
        return false;
    }
}
