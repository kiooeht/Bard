package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class ArtifactMelody extends AbstractMelody
{
    public static final String ID = BardMod.makeID("Artifact");

    public ArtifactMelody()
    {
        super(ID, AbstractCard.CardTarget.SELF);

        notes.add(new BlockNote());
        notes.add(new DebuffNote());
        notes.add(new BlockNote());
    }

    @Override
    public void play()
    {
        addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new ArtifactMelody();
    }
}
