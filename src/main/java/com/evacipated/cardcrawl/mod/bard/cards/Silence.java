package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.SilencedBuffsPower;
import com.evacipated.cardcrawl.mod.bard.powers.SilencedDebuffsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Silence extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Silence");
    public static final String IMG = null;
    private static final int COST = 1;

    public Silence()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);

        exhaust = true;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new DebuffNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(m, p, new SilencedDebuffsPower(m)));
        if (upgraded) {
            addToBottom(new ApplyPowerAction(m, p, new SilencedBuffsPower(m)));
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Silence();
    }
}
