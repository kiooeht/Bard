package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class AbstractBardRelic extends AbstractRelic
{
    public AbstractCard.CardColor color;

    public AbstractBardRelic(String setId, String imgName, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx)
    {
        this(setId, imgName, tier, sfx, null);
    }

    public AbstractBardRelic(String setId, String imgName, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx, AbstractCard.CardColor color)
    {
        super(setId, "", tier, sfx);

        this.color = color;

        if (imgName.startsWith("test")) {
            img = ImageMaster.loadImage("images/relics/" + imgName);
            largeImg = ImageMaster.loadImage("images/largeRelics/" + imgName);
            outlineImg = ImageMaster.loadImage("images/relics/outline/" + imgName);
        }
        if (img == null || outlineImg == null) {
            img = ImageMaster.loadImage(BardMod.assetPath("images/relics/" + imgName));
            largeImg = ImageMaster.loadImage(BardMod.assetPath("images/largeRelics/" + imgName));
            outlineImg = ImageMaster.loadImage(BardMod.assetPath("images/relics/outline/" + imgName));
        }
    }

    @Override
    public void loadLargeImg()
    {
        if (largeImg == null) {
            if (imgUrl.startsWith("test")) {
                largeImg = ImageMaster.loadImage("images/largeRelics/" + imgUrl);
            }
            if (largeImg == null) {
                largeImg = ImageMaster.loadImage(BardMod.assetPath("images/largeRelics/" + imgUrl));
            }
        }
    }
}
