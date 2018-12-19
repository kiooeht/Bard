package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.CardIgnore;
import com.evacipated.cardcrawl.mod.bard.Procedure;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

@CardIgnore
public class MelodyCard extends AbstractCard
{
    public static final String ID = BardMod.makeID("MelodyCard");
    public static final String IMG = null;
    private static final int COST = -2;

    public List<AbstractNote> notes;
    private Procedure playCallback;

    public MelodyCard(String name, String description, List<AbstractNote> notes, CardType type)
    {
        this(name, description, notes, type, CardTarget.NONE, null);
    }

    public MelodyCard(String name, String description, List<AbstractNote> notes, CardTarget target, Procedure playCallback)
    {
        this(name, description, notes, CardType.POWER, target, playCallback);
    }

    public MelodyCard(String name, String description, List<AbstractNote> notes, CardType type, CardTarget target, Procedure playCallback)
    {
        super(ID, name, null, IMG, COST, description, type, Bard.Enums.COLOR, CardRarity.SPECIAL, target);

        this.notes = notes;
        this.playCallback = playCallback;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (playCallback != null) {
            playCallback.invoke();
        }
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
        return new MelodyCard(name, rawDescription, notes, target, playCallback);
    }
}
