package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.IncreaseMaxNotesAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class SixFourTime extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("SixFourTime");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int NOTES = 2;
    private static final int UPGRADE_NOTES = 2;

    public SixFourTime()
    {
        super(ID, IMG, COST, CardType.POWER, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = NOTES;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new IncreaseMaxNotesAction(magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            ++timesUpgraded;
            upgraded = true;
            name = EXTENDED_DESCRIPTION[0];
            initializeTitle();
            upgradeMagicNumber(UPGRADE_NOTES);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SixFourTime();
    }
}
