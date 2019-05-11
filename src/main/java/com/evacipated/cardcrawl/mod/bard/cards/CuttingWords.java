package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Collections;
import java.util.List;

public class CuttingWords extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("CuttingWords");
    private static final int COST = 1;
    private static final int DEBUFF = 1;
    private static final int UPGRADE_DEBUFF = 1;

    public CuttingWords()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        magicNumber = baseMagicNumber = DEBUFF;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(DebuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int count = BardMod.getNoteQueue(p).count(DebuffNote.class);
        if (count > 0) {
            for (int i=0; i<count; ++i) {
                AbstractMonster mo = AbstractDungeon.getRandomMonster();
                addToBottom(new ApplyPowerAction(mo, p, new WeakPower(mo, magicNumber, false), magicNumber, true));
                addToBottom(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false), magicNumber, true));
            }
        }

        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        int count = BardMod.getNoteQueue(AbstractDungeon.player).count(DebuffNote.class);
        rawDescription = DESCRIPTION;
        rawDescription += EXTENDED_DESCRIPTION[0] + count + EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard()
    {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DEBUFF);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new CuttingWords();
    }
}
