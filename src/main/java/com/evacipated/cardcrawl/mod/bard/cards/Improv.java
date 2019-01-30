package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.powers.ImprovisationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Improv extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Improv");
    public static final String IMG = null;
    private static final int COST = 0;
    private static final int NOTES_PER_MELODY = 6;
    private static final int UPGRADE_NOTES_PER_MELODY = -1;

    public Improv()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.SELF);

        magicNumber = baseMagicNumber = NOTES_PER_MELODY;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        // TODO make melody not consume notes
        //addToBottom(new SelectMelodyAction(MelodyManager.getAllMelodies()));
        addToBottom(new ApplyPowerAction(p, p, new ImprovisationPower(p, magicNumber, 1), 1));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_NOTES_PER_MELODY);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Improv();
    }
}
