package com.evacipated.cardcrawl.mod.bard.vfx.cardManip;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;

public class ShowThisCardAndAddToDrawPileEffect extends ShowCardAndAddToDrawPileEffect
{
    public ShowThisCardAndAddToDrawPileEffect(AbstractCard srcCard, AbstractCard displayCopy, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom)
    {
        super(srcCard, x, y, randomSpot, cardOffset, toBottom);

        ReflectionHacks.setPrivate(this, ShowCardAndAddToDrawPileEffect.class, "card", displayCopy);

        if (cardOffset) {
            identifySpawnLocation(x, y);
        } else {
            displayCopy.target_x = x;
            displayCopy.target_y = y;
        }
        displayCopy.drawScale = 0.01f;
        displayCopy.targetDrawScale = 1.0f;
    }

    @SpireOverride
    protected void identifySpawnLocation(float x, float y)
    {
        SpireSuper.call(x, y);
    }
}
