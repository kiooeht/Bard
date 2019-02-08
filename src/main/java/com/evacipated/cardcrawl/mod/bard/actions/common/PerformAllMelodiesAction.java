package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.cards.MelodyCard;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.patches.CenterGridCardSelectScreen;
import com.evacipated.cardcrawl.mod.bard.patches.ConfirmationGridCardSelectCallback;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

import java.util.List;

public class PerformAllMelodiesAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(BardMod.makeID("PerformAllMelodiesAction"));
    private static final String[] TEXT = uiStrings.TEXT;
    private List<AbstractMelody> melodies;
    private boolean consumeNotes;

    public PerformAllMelodiesAction()
    {
        this(null);
    }

    public PerformAllMelodiesAction(List<AbstractMelody> melodies)
    {
        this(melodies, true);
    }

    public PerformAllMelodiesAction(List<AbstractMelody> melodies, boolean consumeNotes)
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
                if (AbstractDungeon.player instanceof Bard) {
                    melodies = ((Bard) AbstractDungeon.player).getPlayableMelodies();
                }
                if (melodies == null || melodies.isEmpty()) {
                    isDone = true;
                    return;
                }
            }

            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractMelody melody : melodies) {
                group.addToTop(melody.makeChoiceCard());
            }

            CenterGridCardSelectScreen.centerGridSelect = true;
            ConfirmationGridCardSelectCallback.callback = cardGroup -> {
                ConfirmationGridCardSelectCallback.callback = null;
                CenterGridCardSelectScreen.centerGridSelect = false;

                for (AbstractCard c : cardGroup.group) {
                    MelodyCard select = (MelodyCard) c;
                    // TODO: Overlapping melodies consume extra notes
                    select.consumeNotes = consumeNotes;
                    select.use(AbstractDungeon.player, null);
                }
            };
            AbstractDungeon.gridSelectScreen.openConfirmationGrid(group, melodies.size() == 1 ? TEXT[0] : TEXT[1]);
            AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
        }
        tickDuration();
    }
}
