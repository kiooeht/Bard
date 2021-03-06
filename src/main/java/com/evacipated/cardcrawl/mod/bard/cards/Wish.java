package com.evacipated.cardcrawl.mod.bard.cards;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.WishAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Wish extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Wish");
    private static final int COST = -2;
    private static final int UPGRADE_COST = 0;
    private static final String ___ = new String(new byte[]{0x52, 0x65, 0x69, 0x6e, 0x61});

    public Wish()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.NONE);

        if (CardCrawlGame.playerName.equals(___)) {
            loadAnimationByID(ID + "r", 0.2f);
            setCardImage(getCurrentFrame());
        }

        isEthereal = true;
        purgeOnUse = true;
    }

    @Override
    protected Texture getPortraitImage()
    {
        if (CardCrawlGame.playerName.equals(___)) {
            return ImageMaster.loadImage(BardMod.assetPath(String.format("images/1024Portraits/%sr0.png", getBaseImagePath(cardID, type))));
        }
        if (upgraded) {
            return ImageMaster.loadImage(BardMod.assetPath(String.format("images/1024Portraits/%s2.png", getBaseImagePath(cardID, type))));
        }
        return super.getPortraitImage();
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded) {
            return true;
        }
        cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded) {
            addToBottom(new WishAction(uuid));
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            isEthereal = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            if (!CardCrawlGame.playerName.equals(___)) {
                loadCardImage(getRegionName(ID + "2", type).name);
            }
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Wish();
    }
}
