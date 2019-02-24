package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class MagnumOpus extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("MagnumOpus");
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int INSPIRATION = 25;
    private static final int INCREASE_INSPIRATION = 25;

    public MagnumOpus()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.SELF);

        misc = inspiration = baseInspiration = INSPIRATION;
        magicNumber2 = baseMagicNumber2 = INCREASE_INSPIRATION;
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
        addToBottom(new IncreaseMiscAction(uuid, misc, magicNumber2));
        addToBottom(new ApplyPowerAction(p, p, new InspirationPower(p, 1, inspiration), 1));
    }

    @Override
    public void applyPowers()
    {
        inspiration = baseInspiration = misc;
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new MagnumOpus();
    }
}
