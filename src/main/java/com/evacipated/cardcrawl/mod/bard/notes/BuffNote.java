package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class BuffNote extends AbstractNote
{
    @SpireEnum(name="BARD_BUFF_NOTE_TAG") public static AbstractCard.CardTags TAG;

    private static BuffNote singleton;

    public static BuffNote get()
    {
        if (singleton == null) {
            singleton = new BuffNote();
        }
        return singleton;
    }

    private BuffNote()
    {
        super(Color.valueOf("5ba16e"));
    }

    @Override
    public String name()
    {
        return "Buff";
    }

    @Override
    public String ascii()
    {
        return "U";
    }

    @Override
    public AbstractCard.CardTags cardTag()
    {
        return TAG;
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture()
    {
        return BardMod.noteAtlas.findRegion("noteBuff");
    }

    @Override
    public void render(SpriteBatch sb, float x, float y)
    {
        super.render(sb, x, y + 4 * Settings.scale);
    }
}
