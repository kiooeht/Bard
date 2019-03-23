package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.DeEnergizedPower;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.List;

public class Accelerando extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Accelerando");
    private static final int COST = 0;
    private static final int ENERGY = 1;
    private static final int DRAW = 2;

    public Accelerando()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = DRAW;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                new BuffNote(),
                new DebuffNote()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new GainEnergyAction(ENERGY));
        addToBottom(new DrawCardAction(p, magicNumber));
        addToBottom(new ApplyPowerAction(p, p, new DeEnergizedPower(p, ENERGY), ENERGY));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION + DESCRIPTION;
            initializeDescription();
            AlwaysRetainField.alwaysRetain.set(this, true);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Accelerando();
    }
}
