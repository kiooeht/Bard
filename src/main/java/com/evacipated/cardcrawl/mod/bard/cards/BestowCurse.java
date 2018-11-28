package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.CursedPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class BestowCurse extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("BestowCurse");
    public static final String IMG = null;
    private static final int COST = 2;
    private static final int CURSE_AMT = 4;
    private static final int UPGRADE_CURSE_AMT = 3;

    public BestowCurse()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);

        magicNumber = baseMagicNumber = CURSE_AMT;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new DebuffNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(m, p, new CursedPower(m, p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_CURSE_AMT);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BestowCurse();
    }
}
