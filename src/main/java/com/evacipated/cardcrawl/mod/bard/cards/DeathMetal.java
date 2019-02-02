package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.powers.DeathMetalPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class DeathMetal extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("DeathMetal");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int DAMAGE = 2;
    private static final int UPGRADE_DAMAGE = 2;

    public DeathMetal()
    {
        super(ID, IMG, COST, CardType.POWER, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.SELF);

        magicNumber = baseMagicNumber = DAMAGE;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(p, p, new DeathMetalPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DAMAGE);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new DeathMetal();
    }
}
