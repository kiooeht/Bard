package com.evacipated.cardcrawl.mod.bard.cards;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@AutoAdd.Ignore
public class NoteCard extends AbstractCard
{
    public static final String ID = BardMod.makeID("NoteCard");
    public static final String IMG = null;
    private static final int COST = -2;

    public AbstractNote note;

    public NoteCard(String name, String description, AbstractNote note, CardType type)
    {
        super(ID, name, null, IMG, COST, description, type, Bard.Enums.COLOR, CardRarity.SPECIAL, CardTarget.NONE);

        this.note = note;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new NoteCard(name, rawDescription, note, type);
    }
}
