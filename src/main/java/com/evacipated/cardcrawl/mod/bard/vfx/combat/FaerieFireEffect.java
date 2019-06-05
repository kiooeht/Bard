package com.evacipated.cardcrawl.mod.bard.vfx.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FaerieFireEffect extends AbstractGameEffect
{
    @Override
    public void update()
    {
        for (int i=0; i<100; ++i) {
            AbstractDungeon.effectsQueue.add(
                    new FaerieFireParticle(
                            MathUtils.random(0, Settings.WIDTH),
                            Settings.HEIGHT
                    )
            );
        }
        isDone = true;
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
