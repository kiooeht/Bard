package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.GambleAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Gamble extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Gamble");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public Gamble()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.SELF);
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new GambleAction());
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Gamble();
    }
}
