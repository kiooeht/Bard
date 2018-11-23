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
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Inspire extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Inspire");
    public static final String IMG = null;
    private static final int COST = 0;
    private static final int INSPIRATION = 25;

    public Inspire()
    {
        this(0);
    }

    public Inspire(int upgrades)
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.BASIC, CardTarget.SELF);

        timesUpgraded = upgrades;

        magicNumber = baseMagicNumber = INSPIRATION;
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
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new InspirationPower(p, 1, magicNumber), 1));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();

            upgradeMagicNumber(INSPIRATION);
            ExhaustiveFields.baseExhaustive.set(this, 2);
            ExhaustiveFields.exhaustive.set(this, 2);
            ExhaustiveFields.isExhaustiveUpgraded.set(this, true);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Inspire(timesUpgraded);
    }
}
