package com.evacipated.cardcrawl.mod.bard.notes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

import java.util.List;

public class WildCardNote extends AbstractNote
{
    @SpireEnum(name="BARD_WILD_NOTE_TAG") public static AbstractCard.CardTags TAG;

    private static WildCardNote singleton;

    public static WildCardNote get()
    {
        if (singleton == null) {
            singleton = new WildCardNote();
        }
        return singleton;
    }

    private static final float NOTE_TIME = 0.16f;
    private static final float COLOR_TIME = 0.24f;

    private float timer = 0;
    private int noteType = 0;
    private float timerColor = 0;
    private int noteColor = 0;
    private int prev_noteColor = 0;

    private WildCardNote()
    {
        super(Color.valueOf("ffffff"));
    }

    public void update()
    {
        timer += Gdx.graphics.getDeltaTime();
        if (timer > NOTE_TIME) {
            timer -= NOTE_TIME;
            int nextNoteType = noteType;
            while (nextNoteType == noteType) {
                nextNoteType = MathUtils.random(MelodyManager.getAllNotesCount() - 1);
            }
            noteType = nextNoteType;
        }

        timerColor += Gdx.graphics.getDeltaTime();
        if (timerColor > COLOR_TIME) {
            timerColor -= COLOR_TIME;
            prev_noteColor = noteColor;
            noteColor = MathUtils.random(MelodyManager.getAllNotesCount() - 1);

            List<AbstractNote> allNotes = MelodyManager.getAllNotes();
            color = allNotes.get(noteColor).color();
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
    public AbstractCard.CardTags cardTag()
    {
        return TAG;
    }

    @Override
    public Color color()
    {
        return prevColor().cpy().lerp(color, timerColor / COLOR_TIME);
    }

    private Color prevColor()
    {
        List<AbstractNote> allNotes = MelodyManager.getAllNotes();
        return allNotes.get(prev_noteColor).color();
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
