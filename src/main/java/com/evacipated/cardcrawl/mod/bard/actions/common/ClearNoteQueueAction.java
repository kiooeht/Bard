package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ClearNoteQueueAction extends AbstractGameAction
{
    public ClearNoteQueueAction()
    {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_XFAST) {
            if (AbstractDungeon.player instanceof Bard) {
                ((Bard) AbstractDungeon.player).clearNoteQueue();
            }
            isDone = true;
        }
    }
}
