package com.evacipated.cardcrawl.mod.bard.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractBardPower extends AbstractPower
{
    private TextureAtlas powerAtlas = BardMod.assets.loadAtlas(BardMod.assetPath("images/powers/powers.atlas"));

    @Override
    protected void loadRegion(String fileName)
    {
        region48 = powerAtlas.findRegion("48/" + fileName);
        region128 = powerAtlas.findRegion("128/" + fileName);

        if (region48 == null && region128 == null) {
            super.loadRegion(fileName);
        }
    }
}
