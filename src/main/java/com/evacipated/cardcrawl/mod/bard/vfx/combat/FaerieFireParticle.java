package com.evacipated.cardcrawl.mod.bard.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FaerieFireParticle extends AbstractGameEffect
{
    private TextureAtlas.AtlasRegion img;
    private float x;
    private float y;

    private boolean movingRight;
    private float speedX;
    private float speedY;

    public FaerieFireParticle(float x, float y)
    {
        img = ImageMaster.GLOW_SPARK_2;

        this.x = x;
        this.y = y + MathUtils.random(0, 400 * Settings.scale);

        movingRight = MathUtils.randomBoolean();
        speedX = MathUtils.random(40, 80) * Settings.scale;
        speedY = Settings.HEIGHT + MathUtils.random(300 * Settings.scale);

        color = Color.valueOf("e867ed");
        duration = startingDuration = Settings.ACTION_DUR_LONG;
    }

    @Override
    public void update()
    {
        if (movingRight) {
            x += speedX * Gdx.graphics.getDeltaTime();
        } else {
            x -= speedX * Gdx.graphics.getDeltaTime();
        }
        y -= speedY * Gdx.graphics.getDeltaTime();

        speedX -= 120 * Settings.scale * Gdx.graphics.getDeltaTime();
        if (speedX < 0) {
            speedX = MathUtils.random(40, 80) * Settings.scale;
            movingRight = !movingRight;
        }
        speedY += MathUtils.random(-200 * Settings.scale, 200 * Settings.scale);

        super.update();
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (!isDone) {
            sb.setColor(color);
                sb.draw(
                        img,
                        x,
                        y,
                        img.packedWidth / 2f,
                        img.packedHeight / 2f,
                        img.packedWidth,
                        img.packedHeight,
                        Settings.scale,
                        Settings.scale,
                        rotation
                );
        }
    }

    @Override
    public void dispose()
    {

    }
}
