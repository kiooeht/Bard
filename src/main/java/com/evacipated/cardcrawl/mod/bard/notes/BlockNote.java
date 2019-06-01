package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class BlockNote extends AbstractNote
{
    @SpireEnum(name="BARD_BLOCK_NOTE_TAG") public static AbstractCard.CardTags TAG;

    private static BlockNote singleton;

    public static BlockNote get()
    {
        if (singleton == null) {
            singleton = new BlockNote();
        }
        return singleton;
    }

    private BlockNote()
    {
        super(Color.valueOf("99f1fb"));
    }

    @Override
    public String name()
    {
        return "Block";
    }

    @Override
    public String ascii()
    {
        return "B";
    }

    @Override
    public AbstractCard.CardTags cardTag()
    {
        return TAG;
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture()
    {
        return BardMod.noteAtlas.findRegion("noteBlock");
    }
}
