package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RemoveNoteFromQueueAction extends AbstractGameAction
{
    private int startIndex;
    private int count;
    private boolean currentlyRemoving = false;

    public RemoveNoteFromQueueAction(int startIndex, int count)
    {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.startIndex = startIndex;
        this.count = count;
    }

    public void removed(int index)
    {
        if (currentlyRemoving || count <= 0) {
            return;
        }

        if (index >= startIndex && index < startIndex + count) {
            // Removed a note in the middle of this
            --count;
        } else if (index >= 0 && index < startIndex) {
            // Removed a note before this
            --startIndex;
        }
    }

    @Override
    public void update()
    {
        if (count > 0 && AbstractDungeon.player instanceof Bard) {
            currentlyRemoving = true;
            for (int i=0; i<count; ++i) {
                ((Bard) AbstractDungeon.player).removeNoteFromQueue(startIndex);
            }
            currentlyRemoving = false;

            // Refresh cards that care about notes in queue
            AbstractDungeon.player.hand.applyPowers();
        }
        isDone = true;
    }
}
