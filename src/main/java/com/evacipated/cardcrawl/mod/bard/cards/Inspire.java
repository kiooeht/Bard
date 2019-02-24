package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField.ExhaustiveFields;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Inspire extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Inspire");
    private static final int COST = 0;
    private static final int INSPIRATION = 50;
    private static final int UPGRADE_INSPIRATION = 25;

    public Inspire()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.BASIC, CardTarget.SELF);

        inspiration = baseInspiration = INSPIRATION;
        exhaust = true;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new BuffNote());
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
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();

            upgradeInspiration(UPGRADE_INSPIRATION);
            exhaust = false;
            ExhaustiveFields.baseExhaustive.set(this, 2);
            ExhaustiveFields.exhaustive.set(this, 2);
            ExhaustiveFields.isExhaustiveUpgraded.set(this, true);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Inspire();
    }
}
