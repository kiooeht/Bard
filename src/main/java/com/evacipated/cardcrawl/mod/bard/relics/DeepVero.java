package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.ClearNoteQueueAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DeepVero extends AbstractBardRelic
{
    public static final String ID = BardMod.makeID("DeepVero");

    public DeepVero()
    {
        super(ID, RelicTier.BOSS, LandingSound.HEAVY, Bard.Enums.COLOR);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    @Override
    public void onPlayerEndTurn()
    {
        addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBottom(new ClearNoteQueueAction());
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DeepVero();
    }
}
