package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.SoothingSongAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class SoothingSong extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("SoothingSong");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int HEAL = 3;
    private static final int UPGRADE_HEAL = 1;

    public SoothingSong()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = HEAL;
        exhaust = true;
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
        addToBottom(new SoothingSongAction(magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_HEAL);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SoothingSong();
    }
}
