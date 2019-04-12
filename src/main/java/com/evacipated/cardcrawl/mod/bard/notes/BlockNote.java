package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;

public class BlockNote extends AbstractNote
{
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
    public TextureAtlas.AtlasRegion getTexture()
    {
        return BardMod.noteAtlas.findRegion("noteBlock");
    }
}
