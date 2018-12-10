package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.List;

public class Heroism extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Heroism");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int TEMP_HP = 5;
    private static final int UPGRADE_TEMP_HP = 3;
    private static final int INSPIRATION = 50;

    public Heroism()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = TEMP_HP;
        magicNumber2 = baseMagicNumber2 = INSPIRATION;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                new BlockNote(),
                new BuffNote()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new AddTemporaryHPAction(p, p, magicNumber));
        addToBottom(new ApplyPowerAction(p, p, new InspirationPower(p, 1, magicNumber2)));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_TEMP_HP);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Heroism();
    }
}
