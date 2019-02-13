package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PitchPipe extends AbstractBardRelic
{
    public static final String ID = BardMod.makeID("PitchPipe");

    public PitchPipe()
    {
        super(ID, "test5.png", RelicTier.STARTER, LandingSound.CLINK, Bard.Enums.COLOR);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle()
    {
        if (AbstractDungeon.player instanceof Bard) {
            int r = AbstractDungeon.relicRng.random(0, 3);
            AbstractNote note;
            switch (r) {
                case 0:
                    note = new AttackNote();
                    break;
                case 1:
                    note = new BlockNote();
                    break;
                case 2:
                    note = new BuffNote();
                    break;
                case 3:
                    note = new DebuffNote();
                    break;
                default:
                    note = new AttackNote();
            }
            ((Bard) AbstractDungeon.player).noteQueue.queue(note);
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new PitchPipe();
    }
}
