package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import java.util.Arrays;
import java.util.List;

public class Invisibility extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Invisibility");
    public static final String IMG = null;
    private static final int COST = 2;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;

    public Invisibility()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.SELF);

        magicNumber = baseMagicNumber = MAGIC;
        exhaust = true;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                new BuffNote(),
                new BuffNote()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber), magicNumber));
        addToBottom(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            ++timesUpgraded;
            upgraded = true;
            name = EXTENDED_DESCRIPTION[0];
            initializeTitle();
            upgradeMagicNumber(UPGRADE_MAGIC);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Invisibility();
    }
}
