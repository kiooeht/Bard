package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction;
import com.evacipated.cardcrawl.mod.bard.actions.common.SelectMelodyAction;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;

public class QuickSongAction extends AbstractGameAction
{
    public QuickSongAction(int discardAmt)
    {
        amount = discardAmt;
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.DISCARD;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                isDone = true;
                return;
            }

            AbstractPlayer p = AbstractDungeon.player;
            if (p.hand.size() == 0) {
                isDone = true;
                return;
            }

            if (p.hand.size() <= amount) {
                amount = p.hand.size();
                for (int i=0; i<amount; ++i) {
                    AbstractCard c = p.hand.getTopCard();
                    doDiscardAndQueue(c);
                }
                AbstractDungeon.player.hand.applyPowers();
                AbstractDungeon.actionManager.addToBottom(new SelectMelodyAction());
                tickDuration();
                return;
            }

            AbstractDungeon.handCardSelectScreen.open(DiscardAction.TEXT[0], amount, false);
            p.hand.applyPowers();
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                doDiscardAndQueue(c);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.actionManager.addToBottom(new SelectMelodyAction());
        }

        tickDuration();
    }

    private void doDiscardAndQueue(AbstractCard card)
    {
        // Discard
        AbstractDungeon.player.hand.moveToDiscardPile(card);
        card.triggerOnManualDiscard();
        GameActionManager.incrementDiscard(false);
        // Queue notes
        List<AbstractNote> notes = AbstractBardCard.determineNoteTypes(card);
        for (AbstractNote note : notes) {
            AbstractDungeon.actionManager.addToBottom(new QueueNoteAction(note));
        }
    }
}
