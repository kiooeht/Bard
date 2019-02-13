package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
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
            AbstractPlayer p = AbstractDungeon.player;
            if (p instanceof Bard) {
                ((Bard) p).noteQueue.increaseMaxNotes(amount);
            }
        }
        tickDuration();
    }
}
