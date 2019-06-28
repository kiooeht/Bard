package com.evacipated.cardcrawl.mod.bard.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class PlayerFadeEffect extends AbstractGameEffect
{
    private Color originalColor;
    private Color targetColor;
    private float fadeOutTime;
    private float holdTime;
    private float fadeInTime;

    public PlayerFadeEffect(float fadeOutTime, float holdTime, float fadeInTime)
    {
        this.fadeOutTime = fadeOutTime;
        this.holdTime = holdTime;
        this.fadeInTime = fadeInTime;

        duration = startingDuration = fadeOutTime + holdTime + fadeInTime;

        originalColor = AbstractDungeon.player.tint.color.cpy();
        targetColor = originalColor.cpy();
        targetColor.a = 0.1f;
    }

    @Override
    public void update()
    {
        if (duration < fadeInTime) {
            AbstractDungeon.player.tint.color = originalColor.cpy().lerp(targetColor, duration / fadeInTime);
        } else if (duration < fadeInTime + holdTime) {
            AbstractDungeon.player.tint.color = targetColor.cpy();
        } else {
            float t = (duration - (fadeInTime + holdTime)) / fadeOutTime;
            AbstractDungeon.player.tint.color = targetColor.cpy().lerp(originalColor, t);
        }

        duration -= Gdx.graphics.getDeltaTime();
        if (duration <= 0) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {

    }

    @Override
    public void dispose()
    {

    }
}
