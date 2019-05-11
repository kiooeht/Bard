package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.HarmoniousVoicePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class HarmoniousVoice extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("HarmoniousVoice");
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int TEMP_HP_AMT = 1;

    public HarmoniousVoice()
    {
        super(ID, COST, CardType.POWER, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = TEMP_HP_AMT;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(BuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(p, p, new HarmoniousVoicePower(p, magicNumber), magicNumber));
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
        return new HarmoniousVoice();
    }
}
