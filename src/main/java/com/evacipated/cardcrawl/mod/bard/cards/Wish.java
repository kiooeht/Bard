package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.WishAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Wish extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Wish");
    public static final String IMG = null;
    private static final int COST = -2;
    private static final int UPGRADE_COST = 0;

    public Wish()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.NONE);

        purgeOnUse = true;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded) {
            return true;
        }
        cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded) {
            addToBottom(new WishAction(uuid));
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Wish();
    }
}
