package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.LocateObjectAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class LocateObject extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("LocateObject");
    public static final String IMG = null;
    private static final int COST = 0;
    private static final int CARD_COUNT = 1;
    private static final int EXHAUSTIVE = 1;
    private static final int UPGRADE_EXHAUSTIVE = 1;

    public LocateObject()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.NONE);

        magicNumber = baseMagicNumber = CARD_COUNT;
        exhaust = true;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new LocateObjectAction(magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            ExhaustiveVariable.setBaseValue(this, EXHAUSTIVE);
            ExhaustiveVariable.upgrade(this, UPGRADE_EXHAUSTIVE);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new LocateObject();
    }
}
