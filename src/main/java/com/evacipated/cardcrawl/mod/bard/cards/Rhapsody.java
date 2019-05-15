package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.SelectNoteAction;
import com.evacipated.cardcrawl.mod.bard.actions.unique.RhapsodyAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Rhapsody extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Rhapsody");
    private static final int COST = 1;

    public Rhapsody()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded) {
            addToBottom(new SelectNoteAction());
        }
        addToBottom(new RhapsodyAction());
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION + DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Rhapsody();
    }
}
