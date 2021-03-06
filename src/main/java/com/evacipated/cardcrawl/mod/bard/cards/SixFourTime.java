package com.evacipated.cardcrawl.mod.bard.cards;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.IncreaseMaxNotesAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class SixFourTime extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("SixFourTime");
    private static final int COST = 1;
    private static final int NOTES = 2;
    private static final int UPGRADE_NOTES = 2;

    public SixFourTime()
    {
        super(ID, COST, CardType.POWER, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = NOTES;
    }

    @Override
    protected Texture getPortraitImage()
    {
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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new IncreaseMaxNotesAction(magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            ++timesUpgraded;
            upgraded = true;
            name = EXTENDED_DESCRIPTION[0];
            initializeTitle();
            upgradeMagicNumber(UPGRADE_NOTES);
            loadCardImage(getRegionName(ID + "2", type).name);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SixFourTime();
    }
}
