package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.PerformAllMelodiesAction;
import com.evacipated.cardcrawl.mod.bard.actions.common.SelectMelodyAction;
import com.evacipated.cardcrawl.mod.bard.cards.MelodyCard;
import com.evacipated.cardcrawl.mod.bard.characters.NoteQueue;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.WildCardNote;
import com.evacipated.cardcrawl.mod.bard.patches.CenterGridCardSelectScreen;
import com.evacipated.cardcrawl.mod.bard.patches.ConfirmationGridCardSelectCallback;
import com.evacipated.cardcrawl.mod.bard.powers.SonataPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RhapsodyAction extends AbstractGameAction
{
    private List<AbstractMelody> melodies;
    private boolean pickCard = false;

    public RhapsodyAction()
    {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_MED) {
            if (melodies == null) {
                melodies = new ArrayList<>();

                List<AbstractNote> allNotes = MelodyManager.getAllNotes();
                List<AbstractMelody> allMelodies = MelodyManager.getAllMelodies();
                NoteQueue noteQueue = BardMod.getNoteQueue(AbstractDungeon.player);
                int wildCount = noteQueue.countExactType(WildCardNote.class);
                for (AbstractMelody melody : allMelodies) {
                    int noteCount = 0;
                    for (AbstractNote note : allNotes) {
                        int melodyCount = melody.count(note.getClass());
                        int queueCount = noteQueue.countExactType(note.getClass());
                        noteCount += Integer.min(melodyCount, queueCount);
                    }
                    if (noteCount + wildCount >= melody.length()) {
                        melodies.add(melody.makeCopy());
                    }
                }

                if (melodies.isEmpty()) {
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
            if (AbstractDungeon.player.hasPower(SonataPower.POWER_ID)) {
                ConfirmationGridCardSelectCallback.callback = cardGroup -> {
                    ConfirmationGridCardSelectCallback.callback = null;
                    CenterGridCardSelectScreen.centerGridSelect = false;

                    for (AbstractCard c : cardGroup.group) {
                        MelodyCard select = (MelodyCard) c;
                        callback(select);
                    }
                };
                AbstractDungeon.gridSelectScreen.openConfirmationGrid(group, melodies.size() == 1 ? PerformAllMelodiesAction.TEXT[0] : PerformAllMelodiesAction.TEXT[1]);
            } else {
                AbstractDungeon.gridSelectScreen.open(group, 1, SelectMelodyAction.TEXT[0], false);
            }
            AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
        } else {
            if (pickCard && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                pickCard = false;
                MelodyCard select = (MelodyCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                CenterGridCardSelectScreen.centerGridSelect = false;

                callback(select);

                isDone = true;
            }
        }
        tickDuration();
    }

    private void callback(MelodyCard select)
    {
        select.consumeNotes = false;
        select.use(AbstractDungeon.player, null);

        // Manually consume notes
        int consumed = 0;
        NoteQueue noteQueue = BardMod.getNoteQueue(AbstractDungeon.player);
        for (AbstractNote melodyNote : select.notes) {
            Iterator<AbstractNote> iter = noteQueue.iterator();
            while (iter.hasNext()) {
                AbstractNote note = iter.next();
                if (note.isNoteExactType(melodyNote.getClass())) {
                    iter.remove();
                    ++consumed;
                    break;
                }
            }
        }

        int wildNotesToConsume = select.notes.size() - consumed;
        if (wildNotesToConsume > 0) {
            Iterator<AbstractNote> iter = noteQueue.iterator();
            while (iter.hasNext() && wildNotesToConsume > 0) {
                AbstractNote note = iter.next();
                if (note.isNoteExactType(WildCardNote.class)) {
                    iter.remove();
                    --wildNotesToConsume;
                }
            }
        }
    }
}
