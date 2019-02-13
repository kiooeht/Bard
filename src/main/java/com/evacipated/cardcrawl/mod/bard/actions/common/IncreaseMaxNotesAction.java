package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class IncreaseMaxNotesAction extends AbstractGameAction
{
    public IncreaseMaxNotesAction(int amount)
    {
        this.amount = amount;
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            BardMod.getNoteQueue(AbstractDungeon.player).increaseMaxNotes(amount);
        }
        tickDuration();
    }
}
