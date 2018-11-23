package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.core.Settings;

public class DebuffNote extends AbstractNote
{
    @Override
    public String name()
    {
        return "Debuff";
    }

    @Override
    public String ascii()
    {
        return "D";
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture()
    {
        return BardMod.noteAtlas.findRegion("debuff");
    }

    @Override
    public TextureAtlas.AtlasRegion getQueuedTexture()
    {
        return BardMod.noteAtlas.findRegion("queuedDebuff");
    }

    @Override
    public void render(SpriteBatch sb, float x, float y)
    {
        super.render(sb, x, y + 18 * Settings.scale);
    }
}
