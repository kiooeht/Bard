package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Greatness extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Greatness");
    private static final int COST = 1;
    private static final int INSPIRATION = 75;
    private static final int UPGRADE_INSPIRATION = 25;

    public Greatness()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        inspiration = baseInspiration = INSPIRATION;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(BuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(p, p, new InspirationPower(p, 1, inspiration), 1));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeInspiration(UPGRADE_INSPIRATION);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Greatness();
    }
}
