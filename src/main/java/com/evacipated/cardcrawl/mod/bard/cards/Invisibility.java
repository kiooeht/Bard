package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.vfx.combat.PlayerFadeEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import java.util.Arrays;
import java.util.List;

public class Invisibility extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Invisibility");
    private static final int COST = 2;
    private static final int INTANGIBLE = 1;
    private static final int DRAW = 1;
    private static final int UPGRADE_DRAW = 1;

    public Invisibility()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.SELF);

        magicNumber2 = baseMagicNumber2 = INTANGIBLE;
        magicNumber = baseMagicNumber = DRAW;
        exhaust = true;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                BuffNote.get(),
                BuffNote.get()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber2), magicNumber2));
        addToBottom(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, magicNumber), magicNumber));
        AbstractDungeon.effectsQueue.add(new PlayerFadeEffect(0.5f, 0.5f, 0.5f));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            ++timesUpgraded;
            upgraded = true;
            name = EXTENDED_DESCRIPTION[0];
            initializeTitle();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            upgradeMagicNumber(UPGRADE_DRAW);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Invisibility();
    }
}
