package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class QueueNoteAction extends AbstractGameAction
{
    protected AbstractNote note;

    public QueueNoteAction(AbstractNote note)
    {
        this.note = note;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            BardMod.getNoteQueue(AbstractDungeon.player).queue(note);
            if (Settings.FAST_MODE) {
                isDone = true;
                return;
            }
        }
        tickDuration();
    }
}
