package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class PutCardInHand extends AbstractGameAction
{
    private static final float PADDING = 25.0F * Settings.scale;

    private AbstractCard card;
    private boolean isOtherCardInCenter = false;

    public PutCardInHand(AbstractCard card, boolean isOtherCardInCenter)
    {
        UnlockTracker.markCardAsSeen(card.cardID);
        this.card = card;
        this.isOtherCardInCenter = isOtherCardInCenter;
        actionType = ActionType.CARD_MANIPULATION;
        duration = 0.35f;
    }

    @Override
    public void update()
    {
        if (isOtherCardInCenter) {
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card,
                    Settings.WIDTH / 2.0f - (PADDING + AbstractCard.IMG_WIDTH), Settings.HEIGHT / 2.0f));
        } else {
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card));
        }

        isDone = true;
    }
}
