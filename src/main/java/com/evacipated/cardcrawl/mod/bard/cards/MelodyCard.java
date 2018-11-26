package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.CardIgnore;
import com.evacipated.cardcrawl.mod.bard.Procedure;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@CardIgnore
public class MelodyCard extends AbstractCard
{
    public static final String ID = BardMod.makeID("MelodyCard");
    public static final String IMG = null;
    private static final int COST = -2;

    private Procedure playCallback;

    public MelodyCard(String name, String description, CardTarget target, Procedure playCallback)
    {
        super(ID, name, null, IMG, COST, description, CardType.POWER, Bard.Enums.COLOR, CardRarity.SPECIAL, target);

        this.playCallback = playCallback;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        playCallback.invoke();
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
        return new MelodyCard(name, rawDescription, target, playCallback);
    }
}
