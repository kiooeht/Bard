package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.Arrays;
import java.util.List;

public class FeastOnFear extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("FeastOnFear");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_BLOCK = 2;

    public FeastOnFear()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        magicNumber = baseMagicNumber = BLOCK;
        baseBlock = BLOCK;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                new DebuffNote(),
                new BlockNote()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m.hasPower(VulnerablePower.POWER_ID)) {
            addToBottom(new AddTemporaryHPAction(p, p, magicNumber));
        } else {
            addToBottom(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            upgradeMagicNumber(UPGRADE_BLOCK);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new FeastOnFear();
    }
}
