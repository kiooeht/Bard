package com.evacipated.cardcrawl.mod.bard.cards.variables;

import basemod.abstracts.DynamicVariable;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class InspirationVariable extends DynamicVariable
{
    @Override
    public String key()
    {
        return "bard:In";
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).baseInspiration;
        }
        return -1;
    }

    @Override
    public int value(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).inspiration;
        }
        return -1;
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).isInspirationModified;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v)
    {
        if (card instanceof AbstractBardCard) {
            ((AbstractBardCard) card).isInspirationModified = v;
        }
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).upgradedInspiration;
        }
        return false;
    }
}
