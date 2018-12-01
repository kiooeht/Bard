package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

import java.util.Collections;
import java.util.List;

public class Parry extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Parry");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int TIMES = 3;
    private static final int UPGRADE_TIMES = 2;

    public Parry()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.SELF);

        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = TIMES;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new BlockNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new GainBlockAction(p, p, block));
    }

    @Override
    public void applyPowers()
    {
        AbstractPower dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
        if (dexterity != null) {
            dexterity.amount *= magicNumber;
        }

        super.applyPowers();

        if (dexterity != null) {
            dexterity.amount /= magicNumber;
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_TIMES);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Parry();
    }
}
