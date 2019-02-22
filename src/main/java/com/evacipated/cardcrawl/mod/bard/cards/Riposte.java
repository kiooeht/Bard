package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.List;

public class Riposte extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Riposte");
    public static final String IMG = BardMod.assetPath("images/cards/attack/riposte.png");
    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int BLOCK = 5;
    private static final int UPGRADE_BLOCK = 2;

    public Riposte()
    {
        super(ID, IMG, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.BASIC, CardTarget.ENEMY);

        baseDamage = DAMAGE;
        baseBlock = BLOCK;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                new AttackNote(),
                new BlockNote()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBottom(new WaitAction(0.1f));
        addToBottom(new GainBlockAction(p, p, block));
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
        return new Riposte();
    }
}
