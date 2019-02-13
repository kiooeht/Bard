package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.RestNote;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class Rest extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Rest");
    public static final String IMG = null;
    private static final int COST = 0;
    private static final int HEAL = 2;
    private static final int UPGRADE_HEAL = 1;

    public Rest()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.SELF);

        magicNumber = baseMagicNumber = HEAL;
        exhaust = true;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p instanceof Bard) {
            int count = ((Bard) p).noteQueue.count(RestNote.class);
            for (int i=0; i<count; ++i) {
                addToBottom(new HealAction(p, p, magicNumber));
            }
            ((Bard) p).noteQueue.removeNotesIf(n -> n instanceof RestNote);
        }

        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        int count = 0;
        if (AbstractDungeon.player instanceof Bard) {
            count = ((Bard) AbstractDungeon.player).noteQueue.count(RestNote.class);
        }
        rawDescription = DESCRIPTION;
        rawDescription += EXTENDED_DESCRIPTION[0] + count + EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard()
    {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_HEAL);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Rest();
    }
}
