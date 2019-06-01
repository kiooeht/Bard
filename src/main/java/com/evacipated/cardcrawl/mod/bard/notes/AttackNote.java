package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class AttackNote extends AbstractNote
{
    @SpireEnum(name="BARD_ATTACK_NOTE_TAG") public static AbstractCard.CardTags TAG;

    private static AttackNote singleton;

    public static AttackNote get()
    {
        if (singleton == null) {
            singleton = new AttackNote();
        }
        return singleton;
    }

    private AttackNote()
    {
        super(Color.valueOf("ce6e79"));
    }

    @Override
    public String name()
    {
        return "Attack";
    }

    @Override
    public String ascii()
    {
        return "A";
    }

    @Override
    public AbstractCard.CardTags cardTag()
    {
        return TAG;
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture()
    {
        return BardMod.noteAtlas.findRegion("noteAttack");
    }

    @Override
    public void render(SpriteBatch sb, float x, float y)
    {
        super.render(sb, x, y + 12 * Settings.scale);
    }
}
