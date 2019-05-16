package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.megacrit.cardcrawl.core.Settings;

import java.util.List;

public class WildCardNote extends AbstractNote
{
    private static WildCardNote singleton;

    public static WildCardNote get()
    {
        if (singleton == null) {
            singleton = new WildCardNote();
        }
        return singleton;
    }

    private float timer = 0;
    private int noteType = 0;

    private WildCardNote() {}

    public void update()
    {
        timer += Gdx.graphics.getDeltaTime();
        if (timer > 0.16f) {
            timer -= 0.16f;
            int nextNoteType = noteType;
            while (nextNoteType == noteType) {
                nextNoteType = MathUtils.random(MelodyManager.getAllNotesCount() - 1);
            }
            noteType = nextNoteType;
        }
    }

    @Override
    public String name()
    {
        return "Wild";
    }

    @Override
    public String ascii()
    {
        return "*";
    }

    @Override
    public float floatFactor()
    {
        return 3;
    }

    @Override
    public boolean isNoteType(Class<? extends AbstractNote> type)
    {
        return true;
    }

    @Override
    public TextureAtlas.AtlasRegion getTexture()
    {
        List<AbstractNote> allNotes = MelodyManager.getAllNotes();
        return allNotes.get(noteType).getTexture();
    }

    @Override
    public TextureAtlas.AtlasRegion getQueuedTexture()
    {
        List<AbstractNote> allNotes = MelodyManager.getAllNotes();
        return allNotes.get(noteType).getQueuedTexture();
    }

    @Override
    public void render(SpriteBatch sb, float x, float y)
    {
        super.render(sb, x, y + 12 * Settings.scale);
    }
}
