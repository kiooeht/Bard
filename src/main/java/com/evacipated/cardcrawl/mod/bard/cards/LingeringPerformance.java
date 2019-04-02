package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;
import java.util.List;

public class LingeringPerformance extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("LingeringPerformance");
    private static final int COST = 1;
    private static final int INCREASE = 1;
    private static final int EXHAUSTIVE_USES = 2;
    private static final int UPGRADE_EXHAUSTIVE_USES = 1;

    public LingeringPerformance()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = INCREASE;
        ExhaustiveVariable.setBaseValue(this, EXHAUSTIVE_USES);
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new BuffNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                for (int i=p.powers.size()-1; i>=0; --i) {
                    AbstractPower power = p.powers.get(i);
                    if (power instanceof InspirationPower && power.ID.equals(InspirationPower.POWER_ID)) {
                        AbstractPower clone = ((InspirationPower) power).makeCopy();
                        clone.amount = 1;
                        clone.updateDescription();
                        addToTop(new ApplyPowerAction(clone.owner, p, clone, 1, true));
                    }
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            ExhaustiveVariable.upgrade(this, UPGRADE_EXHAUSTIVE_USES);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new LingeringPerformance();
    }
}
