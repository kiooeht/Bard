package com.evacipated.cardcrawl.mod.bard.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
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
    private static final int COST = -2;

    public AbstractNote note;

    public NoteCard(String name, String description, AbstractNote note, CardType type)
    {
        this(name, null, description, note, type);
    }

    public NoteCard(String name, CustomCard.RegionName img, String description, AbstractNote note, CardType type)
    {
        super(ID, name, null, img != null ? img.name : null, COST, description, type, Bard.Enums.COLOR, CardRarity.SPECIAL, CardTarget.NONE);

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
