package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.BucklerBashAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class BucklerBash extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("BucklerBash");
    private static final int COST = 1;
    private static final int BLOCK_LOSS = 5;
    private static final int UPGRADE_BLOCK_LOSS = 2;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_DAMAGE = 6;

    public BucklerBash()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BLOCK_LOSS;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(AttackNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new BucklerBashAction(m, p, magicNumber, damage, damageTypeForTurn));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_BLOCK_LOSS);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BucklerBash();
    }
}
