package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.MnemonicVestmentsAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class MnemonicVestments extends AbstractBardCard implements StartupCard
{
    public static final String ID = BardMod.makeID("MnemonicVestments");
    private static final int COST = -2;

    public MnemonicVestments()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.NONE);
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        addToBottom(new MnemonicVestmentsAction());
        return true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            isEthereal = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new MnemonicVestments();
    }
}
