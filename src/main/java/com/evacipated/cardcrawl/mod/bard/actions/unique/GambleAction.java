package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.cards.Gamble;
import com.evacipated.cardcrawl.mod.bard.cards.MelodyCard;
import com.evacipated.cardcrawl.mod.bard.patches.CenterGridCardSelectScreen;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GambleAction extends AbstractGameAction
{
    private boolean choosing = true;
    private boolean pickCard = false;
    private AbstractCard.CardType cardType;

    public GambleAction()
    {
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.DRAW;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_XFAST) {
            if (choosing) {
                if (AbstractDungeon.player.drawPile.isEmpty()) {
                    AbstractDungeon.actionManager.addToTop(this);
                    AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
                }

                // TODO: Choose

                choosing = false;
                return;
            } else {
                if (AbstractDungeon.player.drawPile.isEmpty()) {
                    isDone = true;
                    return;
                }

                pickCard = true;
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                group.addToTop(new MelodyCard("Attack", AbstractBardCard.getRegionName(Gamble.ID, AbstractCard.CardType.ATTACK),
                        "Attack", null, AbstractCard.CardType.ATTACK));
                group.addToTop(new MelodyCard("Skill", AbstractBardCard.getRegionName(Gamble.ID, AbstractCard.CardType.SKILL),
                        "Skill", null, AbstractCard.CardType.SKILL));
                group.addToTop(new MelodyCard("Power", AbstractBardCard.getRegionName(Gamble.ID, AbstractCard.CardType.POWER),
                        "Power", null, AbstractCard.CardType.POWER));
                group.group.get(0).color = AbstractCard.CardColor.RED;
                group.group.get(1).color = AbstractCard.CardColor.GREEN;
                group.group.get(2).color = AbstractCard.CardColor.BLUE;

                CenterGridCardSelectScreen.centerGridSelect = true;
                AbstractDungeon.gridSelectScreen.open(group, 1, "Choose Card Type to Gamble on", false);
            }
        } else if (pickCard && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            pickCard = false;
            AbstractCard typeCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            CenterGridCardSelectScreen.centerGridSelect = false;

            AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
            if (card.type == typeCard.type) {
                card.setCostForTurn(0);
            }
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, 1));
            isDone = true;
        }
        tickDuration();
    }
}
