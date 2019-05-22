package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.FocusedAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.stslib.variables.RefundVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.Collections;
import java.util.List;

public class Focused extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Focused");
    private static final int COST = -1;
    private static final int INSPIRATION_PER_ENERGY = 100;
    private static final int REFUND = 0;
    private static final int UPGRADE_REFUND = 1;

    public Focused()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.SELF);


        inspiration = baseInspiration = 0;

        updateDescription();
    }

    private void updateDescription()
    {
        String amount = "";
        if (inspiration > baseInspiration) {
            amount += "+" + (inspiration - baseInspiration);
        } else if (inspiration < baseInspiration) {
            amount += "-" + (baseInspiration - inspiration);
        }
        rawDescription = String.format(DESCRIPTION, amount) + (upgraded ? UPGRADE_DESCRIPTION : "");
        initializeDescription();
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(BuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (energyOnUse < EnergyPanel.totalCount) {
            energyOnUse = EnergyPanel.totalCount;
        }

        addToBottom(new FocusedAction(freeToPlayOnce, energyOnUse, INSPIRATION_PER_ENERGY, inspiration));
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        updateDescription();
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            // These are separate so the refund number is highlighted when previewing upgrades
            RefundVariable.setBaseValue(this, REFUND);
            RefundVariable.upgrade(this, UPGRADE_REFUND);
            updateDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Focused();
    }
}
