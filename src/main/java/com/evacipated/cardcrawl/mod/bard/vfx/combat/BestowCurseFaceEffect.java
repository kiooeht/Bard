package com.evacipated.cardcrawl.mod.bard.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BestowCurseFaceEffect extends AbstractGameEffect
{
    private Texture img;
    private float x;
    private float y;

    public BestowCurseFaceEffect(float x, float y)
    {
        img = BardMod.assets.loadImage(BardMod.assetPath("images/vfx/combat/necromanticTotemFace.png"));

        this.x = x;
        this.y = y;

        color = Color.WHITE.cpy();
        duration = startingDuration = 1.3f;
    }

    @Override
    public void update()
    {
        if (duration >= startingDuration / 2F) {
            //color.a = duration / (startingDuration / 2F);
        }
        super.update();
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(color);
        sb.draw(
                img,
                x - img.getWidth() * Settings.scale / 2,
                y - img.getHeight() * Settings.scale / 2,
                img.getWidth() * Settings.scale,
                img.getHeight() * Settings.scale
        );
    }

    @Override
    public void dispose()
    {
    }
}
