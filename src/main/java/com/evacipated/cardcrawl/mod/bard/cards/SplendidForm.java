package com.evacipated.cardcrawl.mod.bard.cards;

import basemod.helpers.BaseModCardTags;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.SplendidPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class SplendidForm extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("SplendidForm");
    public static final String IMG = null;
    private static final int COST = 3;
    private static final int INSPIRATION = 100;
    private static final int UPGRADE_INSPIRATION = 100;

    public SplendidForm()
    {
        super(ID, IMG, COST, CardType.POWER, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.SELF);

        tags.add(BaseModCardTags.FORM);

        inspiration = baseInspiration = INSPIRATION;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new BuffNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(p, p, new SplendidPower(p, inspiration, 1), 1));
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
        return new SplendidForm();
    }
}
