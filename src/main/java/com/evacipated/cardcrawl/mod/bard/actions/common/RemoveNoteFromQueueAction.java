package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RemoveNoteFromQueueAction extends AbstractGameAction
{
    private int startIndex;
    private int count;

    public RemoveNoteFromQueueAction(int startIndex, int count)
    {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
        this.startIndex = startIndex;
        this.count = count;
    }

    @Override
    public void update()
    {
        if (AbstractDungeon.player instanceof Bard) {
            for (int i=0; i<count; ++i) {
                ((Bard) AbstractDungeon.player).removeNoteFromQueue(startIndex);
            }
        }
        isDone = true;
    }
}
