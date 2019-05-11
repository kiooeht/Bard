package com.evacipated.cardcrawl.mod.bard.cards;

import basemod.helpers.BaseModCardTags;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Defend_Bard extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Defend");
    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_BLOCK = 2;

    public Defend_Bard()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.BASIC, CardTarget.SELF);

        baseBlock = BLOCK;
        tags.add(BaseModCardTags.BASIC_DEFEND);
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(BlockNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (Settings.isDebug) {
            addToBottom(new GainBlockAction(p, p, 50));
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
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Defend_Bard();
    }
}
