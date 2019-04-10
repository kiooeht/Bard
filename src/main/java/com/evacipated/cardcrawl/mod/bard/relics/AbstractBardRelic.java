package com.evacipated.cardcrawl.mod.bard.relics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class AbstractBardRelic extends AbstractRelic
{
    public AbstractCard.CardColor color;

    public AbstractBardRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx)
    {
        this(setId, tier, sfx, null);
    }

    public AbstractBardRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx, AbstractCard.CardColor color)
    {
        super(setId, "", tier, sfx);

        this.color = color;

        String imgName = getBaseImagePath();

        loadImages(BardMod.assetPath(""), imgName);
        if (img == null || outlineImg == null) {
            loadImages("", imgName);
        }
        if (img == null || outlineImg == null) {
            loadImages("", "test5.png");
        }
    }

    protected void loadImages(String basePath, String imgName)
    {
        img = ImageMaster.loadImage(basePath +"images/relics/" + imgName);
        largeImg = ImageMaster.loadImage(basePath +"images/largeRelics/" + imgName);
        outlineImg = ImageMaster.loadImage(basePath + "images/relics/outline/" + imgName);
    }

    protected String getBaseImagePath()
    {
        String id = relicId.replaceFirst("^" + BardMod.makeID(""), "");
        char c[] = id.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        id = new String(c);
        return id + ".png";
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

    public Texture getLargeImgForSingleView()
    {
        if (largeImg != null && largeImg.getTextureData() instanceof FileTextureData) {
            FileHandle file = ((FileTextureData) largeImg.getTextureData()).getFileHandle();
            return ImageMaster.loadImage(file.path());
        }
        return null;
    }

    protected void addToTop(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToTop(action);
    }

    protected void addToBottom(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToBottom(action);
    }
}
