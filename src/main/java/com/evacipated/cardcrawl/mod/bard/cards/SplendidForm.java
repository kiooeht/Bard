package com.evacipated.cardcrawl.mod.bard.cards;

import basemod.helpers.BaseModCardTags;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.SplendidPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class SplendidForm extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("SplendidForm");
    private static final int COST = 3;
    private static final int INSPIRATION = 100;
    private static final int UPGRADE_INSPIRATION = 100;
    private static final String ___ = new String(new byte[]{0x47, 0x72, 0x65, 0x6d, 0x69, 0x6f, 0x75, 0x73});

    public SplendidForm()
    {
        super(ID, COST, CardType.POWER, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.SELF);
        if (CardCrawlGame.playerName.equals(___)) {
            RegionName regionName = getRegionName(cardID, type);
            loadCardImage(regionName.name + "g");
        }

        tags.add(BaseModCardTags.FORM);

        magicNumber2 = baseMagicNumber2 = INSPIRATION;
    }

    @Override
    protected Texture getPortraitImage()
    {
        if (CardCrawlGame.playerName.equals(___)) {
            return ImageMaster.loadImage(BardMod.assetPath(String.format("images/1024Portraits/%sg.png", getBaseImagePath(cardID, type))));
        }
        return super.getPortraitImage();
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(BuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new ApplyPowerAction(p, p, new SplendidPower(p, magicNumber2, 1), 1));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber2(UPGRADE_INSPIRATION);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SplendidForm();
    }
}
