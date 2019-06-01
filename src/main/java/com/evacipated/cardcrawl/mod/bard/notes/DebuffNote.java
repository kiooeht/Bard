package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class DebuffNote extends AbstractNote
{
    @SpireEnum(name="BARD_DEBUFF_NOTE_TAG") public static AbstractCard.CardTags TAG;

    private static DebuffNote singleton;

    public static DebuffNote get()
    {
        if (singleton == null) {
            singleton = new DebuffNote();
        }
        return singleton;
    }

    private DebuffNote()
    {
        super(Color.valueOf("ce9564"));
    }

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
    public AbstractCard.CardTags cardTag()
    {
        return TAG;
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture()
    {
        return BardMod.noteAtlas.findRegion("noteDebuff");
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
