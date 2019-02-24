package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Sift extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Sift");
    private static final int COST = 1;
    private static final int DRAW = 1;

    public Sift()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.SELF);

        magicNumber2 = baseMagicNumber2 = DRAW;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.drawPile.isEmpty()) {
            addToBottom(new EmptyDeckShuffleAction());
        }
        addToBottom(
                new FetchAction(
                        AbstractDungeon.player.drawPile,
                        c -> AbstractDungeon.player.drawPile.group.indexOf(c) > AbstractDungeon.player.drawPile.size() - magicNumber2 - 1,
                        cards -> {
                            for (AbstractCard c : cards) {
                                if (c.costForTurn > 0) {
                                    addToTop(new GainEnergyAction(c.costForTurn));
                                }
                            }
                            AbstractDungeon.player.hand.refreshHandLayout();
                        }
                )
        );
        if (upgraded) {
            addToBottom(new DrawCardAction(p, 1));
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            magicNumber =  baseMagicNumber = DRAW;
            rawDescription = DESCRIPTION + UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Sift();
    }
}
