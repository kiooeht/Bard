package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.cards.NoteCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public abstract class AbstractNote
{
    public abstract String name();
    public abstract String ascii();

    public boolean isFloaty()
    {
        return true;
    }

    public float floatFactor()
    {
        if (isFloaty()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean countsAsNote()
    {
        return true;
    }

    public abstract TextureAtlas.AtlasRegion getTexture();

    public TextureAtlas.AtlasRegion getQueuedTexture()
    {
        return getTexture();
    }

    public void render(SpriteBatch sb, float x, float y)
    {
        sb.setColor(Color.WHITE);
        TextureAtlas.AtlasRegion tex = getQueuedTexture();
        sb.draw(
                tex,
                x,
                y,
                0,
                0,
                tex.getRegionWidth(),
                tex.getRegionHeight(),
                Settings.scale * 2,
                Settings.scale * 2,
                0
        );
    }

    public AbstractCard makeChoiceCard()
    {
        return new NoteCard(name(), "{" + name() + " Note}", this, AbstractCard.CardType.POWER);
    }

    public boolean isNoteType(Class<? extends AbstractNote> type)
    {
        return type.isInstance(this);
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof AbstractNote) {
            if (this instanceof WildCardNote || other instanceof WildCardNote) {
                return true;
            }
            return this.getClass().equals(other.getClass());
        }
        return super.equals(other);
    }
}
