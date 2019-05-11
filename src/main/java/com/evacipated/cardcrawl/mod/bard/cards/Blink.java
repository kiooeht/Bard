package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.BlinkPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Blink extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Blink");
    private static final int COST = 1;
    private static final int DAMAGE_REDUCE = 50;
    private static final int TURNS = 1;
    private static final int UPGRADE_TURNS = 1;

    public Blink()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber2 = baseMagicNumber2 = DAMAGE_REDUCE;
        magicNumber = baseMagicNumber = TURNS;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(BuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(p, p, new BlinkPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_TURNS);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Blink();
    }
}
