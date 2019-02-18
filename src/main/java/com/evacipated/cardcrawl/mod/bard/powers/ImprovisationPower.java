package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.ClearNoteQueueAction;
import com.evacipated.cardcrawl.mod.bard.actions.common.SelectMelodyAction;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.hooks.OnNoteQueuedHook;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ImprovisationPower extends AbstractBardTwoAmountPower implements NonStackablePower, OnNoteQueuedHook
{
    public static final String POWER_ID = BardMod.makeID("Improvisation");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int origNotes;

    public ImprovisationPower(AbstractCreature owner, int notes, int melodies)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = melodies;
        amount2 = origNotes = notes;
        isTurnBased = true;
        updateDescription();
        loadRegion("twoAndFour");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + amount2;
        if (amount2 == 1) {
            description += DESCRIPTIONS[1];
        } else {
            description += DESCRIPTIONS[2];
        }
        description += DESCRIPTIONS[3] + amount;
        if (amount == 1) {
            description += DESCRIPTIONS[4];
        } else {
            description += DESCRIPTIONS[5];
        }
        description += DESCRIPTIONS[6];
    }

    @Override
    public AbstractNote onNoteQueued(AbstractNote note)
    {
        --amount2;
        if (amount2 == 0) {
            flash();
            amount2 = origNotes;
            for (int i=0; i<amount; ++i) {
                AbstractDungeon.actionManager.addToBottom(new SelectMelodyAction(MelodyManager.getAllMelodies(), false));
            }
        }
        updateDescription();
        return note;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        AbstractDungeon.actionManager.addToBottom(new ClearNoteQueueAction());
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public boolean isStackable(AbstractPower power)
    {
        if (power instanceof ImprovisationPower) {
            return ((ImprovisationPower) power).origNotes == origNotes;
        }
        return false;
    }
}
