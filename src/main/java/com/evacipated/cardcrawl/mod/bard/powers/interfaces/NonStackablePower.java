package com.evacipated.cardcrawl.mod.bard.powers.interfaces;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface NonStackablePower
{
    default boolean isStackable(AbstractPower power)
    {
        return false;
    }
}
