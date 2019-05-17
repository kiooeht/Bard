package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.cards.MelodyCard;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.patches.CenterGridCardSelectScreen;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

import java.util.List;

public class SelectMelodyAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(BardMod.makeID("SelectMelodyAction"));
    public static final String[] TEXT = uiStrings.TEXT;
    private List<AbstractMelody> melodies;
    private boolean consumeNotes;
    private boolean pickCard = false;

    public SelectMelodyAction()
    {
        this(null);
    }

    public SelectMelodyAction(List<AbstractMelody> melodies)
    {
        this(melodies, true);
    }

    public SelectMelodyAction(List<AbstractMelody> melodies, boolean consumeNotes)
    {
        this.melodies = melodies;
        this.consumeNotes = consumeNotes;
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_MED) {
            if (melodies == null) {
                melodies = BardMod.getNoteQueue(AbstractDungeon.player).getPlayableMelodies();
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
            AbstractDungeon.gridSelectScreen.open(group, 1, TEXT[0], false);
            AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
        } else {
            if (pickCard && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                pickCard = false;
                MelodyCard select = (MelodyCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                CenterGridCardSelectScreen.centerGridSelect = false;

                select.consumeNotes = consumeNotes;
                select.use(AbstractDungeon.player, null);
                isDone = true;
            }
        }
        tickDuration();

        if (isDone) {
            BardMod.notesPanel.unlock();
        }
    }
}
