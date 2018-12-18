package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class LocateObjectAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("LocateObjectAction");
    public static final String[] TEXT = uiStrings.TEXT;

    public LocateObjectAction(int amount)
    {
        setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : p.drawPile.group) {
                tmp.addToTop(c);
            }
            tmp.sortAlphabetically(true);
            tmp.sortByRarityPlusStatusCardType(true);
            if (tmp.size() == 0 || tmp.size() == 1) {
                isDone = true;
                return;
            }
            if (tmp.size() <= amount) {
                for (int i=0; i<tmp.size(); ++i) {
                    AbstractCard card = tmp.getNCardFromTop(i);
                    moveToTop(card);
                }
                isDone = true;
                return;
            }

            if (amount == 1) {
                AbstractDungeon.gridSelectScreen.open(tmp, amount, TEXT[0], false);
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, amount, TEXT[1], false);
            }
            tickDuration();
            return;
        }

        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.unhover();
                moveToTop(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        tickDuration();
    }

    private void moveToTop(AbstractCard card)
    {
        AbstractDungeon.player.drawPile.removeCard(card);
        AbstractDungeon.player.drawPile.moveToDeck(card, false);
    }
}
