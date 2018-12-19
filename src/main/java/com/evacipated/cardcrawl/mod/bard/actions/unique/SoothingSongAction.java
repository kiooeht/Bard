package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.evacipated.cardcrawl.mod.bard.cards.MelodyCard;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.patches.CenterGridCardSelectScreen;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;

public class SoothingSongAction extends AbstractGameAction
{
    private List<AbstractMelody> melodies;
    private boolean pickCard = false;
    private int healAmt;

    public SoothingSongAction(int healAmt)
    {
        this(null, healAmt);
    }

    public SoothingSongAction(List<AbstractMelody> melodies, int healAmt)
    {
        this.melodies = melodies;
        this.healAmt = healAmt;
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
                if (melodies == null || melodies.isEmpty()) {
                    isDone = true;
                    return;
                }
            }

            pickCard = true;
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractMelody melody : melodies) {
                group.addToTop(melody.makeChoiceCard());
            }

            CenterGridCardSelectScreen.centerGridSelect = true;
            AbstractDungeon.gridSelectScreen.open(group, 1, "Choose a Melody to Play", false);
        } else {
            if (pickCard && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                pickCard = false;
                MelodyCard select = (MelodyCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                CenterGridCardSelectScreen.centerGridSelect = false;

                select.use(AbstractDungeon.player, null);
                AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, select.notes.size() * healAmt));
                isDone = true;
            }
        }
        tickDuration();
    }
}
