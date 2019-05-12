package com.evacipated.cardcrawl.mod.bard.melodies;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BlockSmallMelody extends AbstractMelody
{
    public static final String ID = BardMod.makeID("BlockSmall");

    public BlockSmallMelody()
    {
        super(ID, AbstractCard.CardTarget.SELF);
        type = AbstractCard.CardType.SKILL;
    }

    @Override
    protected CustomCard.RegionName getRegionName()
    {
        return new CustomCard.RegionName("bard/skill/defend");
    }

    @Override
    public void play()
    {
        addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 10));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new BlockSmallMelody();
    }
}
