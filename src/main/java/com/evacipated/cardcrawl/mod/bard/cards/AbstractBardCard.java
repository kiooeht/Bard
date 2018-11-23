package com.evacipated.cardcrawl.mod.bard.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBardCard extends CustomCard
{
    protected final CardStrings cardStrings;
    protected final String NAME;
    protected final String DESCRIPTION;
    protected final String UPGRADE_DESCRIPTION;
    protected final String[] EXTENDED_DESCRIPTION;

    public int baseMagicNumber2 = -1;
    public int magicNumber2 = -1;
    public boolean isMagicNumber2Modified = false;
    public boolean upgradedMagicNumber2 = false;

    public AbstractBardCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, "FAKE TITLE", img, cost, "FAKE DESCRIPTION", type, color, rarity, target);

        cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        name = NAME = cardStrings.NAME;
        rawDescription = DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
        initializeTitle();
        initializeDescription();
    }

    protected void addToTop(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToTop(action);
    }

    protected void addToBottom(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public abstract List<AbstractNote> getNotes();

    public static List<AbstractNote> determineNoteTypes(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).getNotes();
        }

        List<AbstractNote> notes = new ArrayList<>();
        if (isBlockGainingCard(card)) {
            notes.add(new BlockNote());
        }
        if (isDamageDealingCard(card)) {
            notes.add(new AttackNote());
        }
        return notes;
    }

    public static boolean isDamageDealingCard(AbstractCard card)
    {
        return card.baseDamage >= 0;
    }

    public static boolean isBlockGainingCard(AbstractCard card)
    {
        return card.baseBlock >= 0;
    }
}
