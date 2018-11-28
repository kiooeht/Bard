package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.evacipated.cardcrawl.mod.bard.cards.MelodyCard;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;

public class SelectMelodyAction extends AbstractGameAction
{
    private List<AbstractMelody> melodies;
    private boolean pickCard = false;

    public SelectMelodyAction()
    {
        this(null);
    }

    public SelectMelodyAction(List<AbstractMelody> melodies)
    {
        this.melodies = melodies;
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_MED) {
            if (melodies == null) {
                if (AbstractDungeon.player instanceof Bard) {
                    melodies = ((Bard) AbstractDungeon.player).getPlayableMelodies();
                }
                if (melodies == null) {
                    isDone = true;
                    return;
                }
            }

            pickCard = true;
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractMelody melody : melodies) {
                group.addToTop(melody.makeChoiceCard());
            }

            AbstractDungeon.gridSelectScreen.open(group, 1, "Choose a Melody to Play.", false);
        } else {
            if (pickCard && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                pickCard = false;
                MelodyCard select = (MelodyCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();

                select.use(AbstractDungeon.player, null);
                isDone = true;
            }
        }
        tickDuration();
    }
}
