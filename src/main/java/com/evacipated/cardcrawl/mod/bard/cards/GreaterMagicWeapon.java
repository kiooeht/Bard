package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.GreaterMagicWeaponPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class GreaterMagicWeapon extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("GreaterMagicWeapon");
    private static final int COST = 1;
    private static final int VULNERABLE = 1;

    public GreaterMagicWeapon()
    {
        super(ID, COST, CardType.POWER, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = VULNERABLE;
    }

    @Override
    public float getTitleFontSize()
    {
        return 20;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(BuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(p, p, new GreaterMagicWeaponPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION + DESCRIPTION;
            initializeDescription();
            isInnate = true;
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new GreaterMagicWeapon();
    }
}
