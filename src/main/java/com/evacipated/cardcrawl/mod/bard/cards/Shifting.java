package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.ShiftingAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Shifting extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Shifting");
    private static final int COST = 3;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int BLOCK = 12;
    private static final int UPGRADE_BLOCK = 4;

    public Shifting()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.ENEMY);

        baseDamage = DAMAGE;
        baseBlock = BLOCK;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ShiftingAction(m, damageTypeForTurn, damage, block));
        addToBottom(new WaitAction(0.5f));
        addToBottom(new ShiftingAction(m, damageTypeForTurn, damage, block));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeBlock(UPGRADE_BLOCK);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Shifting();
    }
}
