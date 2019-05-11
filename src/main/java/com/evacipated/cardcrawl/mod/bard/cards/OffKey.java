package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.RestNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class OffKey extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("OffKey");
    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int BLOCK = 10;
    private static final int UPGRADE = 3;
    private static final int REST = 2;

    public OffKey()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = REST;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new GainBlockAction(p, p, block));
        addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SHIELD));
        for (int i=0; i<magicNumber; ++i) {
            addToBottom(new QueueNoteAction(RestNote.get()));
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE);
            upgradeBlock(UPGRADE);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new OffKey();
    }
}
