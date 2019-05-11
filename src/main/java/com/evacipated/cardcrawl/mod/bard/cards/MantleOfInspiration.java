package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.MantleOfInspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class MantleOfInspiration extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("MantleOfInspiration");
    private static final int COST = 1;
    private static final int INSPIRATION_GAIN = 25;
    private static final int UPGRADE_INSPIRATION_GAIN = 25;

    public MantleOfInspiration()
    {
        super(ID, COST, CardType.POWER, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber2 = baseMagicNumber2 = INSPIRATION_GAIN;
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
        addToBottom(new ApplyPowerAction(p, p, new MantleOfInspirationPower(p, magicNumber2), magicNumber2));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber2(UPGRADE_INSPIRATION_GAIN);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new MantleOfInspiration();
    }
}
