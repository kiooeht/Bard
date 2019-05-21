package com.evacipated.cardcrawl.mod.bard.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ThrowNoteEffect extends AbstractGameEffect
{
    private static final float TIME = 0.4f;
    private static final float TARGET_JITTER = 50;

    private AbstractNote note;
    private float x;
    private float y;
    private float staggerTimer;

    private float vX;
    private float vY;
    private float rotationSpeed;

    public ThrowNoteEffect(AbstractNote note, float startX, float startY, float targetX, float targetY, float staggerTimer)
    {
        this.note = note;
        this.x = startX;
        this.y = startY;
        this.staggerTimer = staggerTimer;

        rotationSpeed = MathUtils.random(100, 500);
        if (MathUtils.randomBoolean()) {
            rotationSpeed *= -1;
        }

        targetX += MathUtils.random(-TARGET_JITTER, TARGET_JITTER) * Settings.scale;
        targetY += MathUtils.random(-TARGET_JITTER, TARGET_JITTER) * Settings.scale;
        vX = (targetX - x) / TIME;
        vY = (targetY - y) / TIME;
    }

    @Override
    public void update()
    {
        if (staggerTimer > 0) {
            staggerTimer -= Gdx.graphics.getDeltaTime();
            return;
        }

        rotation += rotationSpeed * Gdx.graphics.getDeltaTime();
        x += vX * Gdx.graphics.getDeltaTime();
        y += vY * Gdx.graphics.getDeltaTime();

        if (x < 0 || x > Settings.WIDTH) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        note.renderExact(sb, x, y, rotation);
    }

    @Override
    public void dispose()
    {

    }
}
