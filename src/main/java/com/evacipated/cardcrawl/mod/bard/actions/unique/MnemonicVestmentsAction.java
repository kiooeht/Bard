package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.evacipated.cardcrawl.mod.bard.cards.MnemonicVestments;
import com.evacipated.cardcrawl.mod.bard.vfx.cardManip.ShowThisCardAndAddToDrawPileEffect;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.DiscardPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MnemonicVestmentsAction extends AbstractGameAction
{
    private boolean exhaustive;

    public MnemonicVestmentsAction(boolean exhaustive, int exhaustiveAmount)
    {
        this.exhaustive = exhaustive;
        amount = exhaustiveAmount;
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_MED) {
            AbstractDungeon.gridSelectScreen.open(getCards(), 1, DiscardPileToHandAction.TEXT[0], false);
            tickDuration();
            return;
        }

        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractCard copy = makeAlteredCopy(c);
                AbstractCard displayCopy = makeAlteredCopy(c);

                AbstractDungeon.effectList.add(new ShowThisCardAndAddToDrawPileEffect(
                        copy,
                        displayCopy,
                        Settings.WIDTH / 2.0f,
                        Settings.HEIGHT / 2.0f,
                        true,
                        true,
                        false
                ));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            isDone = true;
        }
        tickDuration();
    }

    private CardGroup getCards()
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (!card.cardID.equals(MnemonicVestments.ID)) {
                group.addToTop(card);
            }
        }
        return group;
    }

    private AbstractCard makeAlteredCopy(AbstractCard card)
    {
        AbstractCard copy = card.makeSameInstanceOf();
        copy.modifyCostForCombat(-999);
        if (exhaustive) {
            if (ExhaustiveField.ExhaustiveFields.baseExhaustive.get(copy) <= 0) {
                copy.rawDescription += " NL Exhaustive !stslib:ex!.";
            }
            ExhaustiveField.ExhaustiveFields.baseExhaustive.set(copy, amount);
            ExhaustiveField.ExhaustiveFields.exhaustive.set(copy, amount);
            copy.exhaust = false;
        } else {
            if (!copy.exhaust) {
                copy.rawDescription += " NL Exhaust.";
            }
            copy.exhaustOnUseOnce = copy.exhaust = true;
        }
        copy.initializeDescription();

        return copy;
    }
}
