package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.core.Settings;

public class RestNote extends AbstractNote
{
    private static RestNote singleton;

    public static RestNote get()
    {
        if (singleton == null) {
            singleton = new RestNote();
        }
        return singleton;
    }

    private RestNote()
    {
        super(Color.valueOf("969696"));
    }

    @Override
    public String name()
    {
        return "Rest";
    }

    @Override
    public String ascii()
    {
        return "R";
    }

    @Override
    public boolean isFloaty()
    {
        return false;
    }

    @Override
    public boolean countsAsNote()
    {
        return false;
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture()
    {
        return BardMod.noteAtlas.findRegion("noteRest");
    }

    @Override
    public void render(SpriteBatch sb, float x, float y)
    {
        super.render(sb, x, y + 4 * Settings.scale);
    }
}
