package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Sing extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Sing");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public Sing()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.BASIC, CardTarget.SELF);
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        if (p instanceof Bard) {
            return ((Bard) p).canPlayMelody();
        }
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p instanceof Bard) {
            List<AbstractMelody> melodies = ((Bard) p).getPlayableMelodies();
            if (!melodies.isEmpty()) {
                melodies.get(0).play();
            }
        }
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
        return new Sing();
    }
}
