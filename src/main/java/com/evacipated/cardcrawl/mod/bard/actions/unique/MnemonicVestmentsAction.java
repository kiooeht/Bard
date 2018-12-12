package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.DiscardPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;

public class MnemonicVestmentsAction extends AbstractGameAction
{
    private boolean exhaustive = false;

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
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, DiscardPileToHandAction.TEXT[0], false);
            tickDuration();
            return;
        }

        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractCard copy = c.makeSameInstanceOf();
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

                // TODO: new "Exhaust/Exhaustive" isn't shown on card until it's in the draw pile
                AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(
                        copy,
                        Settings.WIDTH / 2.0f,
                        Settings.HEIGHT / 2.0f,
                        true,
                        true,
                        false
                ));
            }

            isDone = true;
        }
        tickDuration();
    }
}
