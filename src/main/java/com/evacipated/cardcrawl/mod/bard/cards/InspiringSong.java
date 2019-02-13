package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class InspiringSong extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("InspiringSong");
    public static final String IMG = null;
    private static final int COST = 1;
    private static final int INSPIRATION = 50;
    private static final int UPGRADE_INSPIRATION = 25;

    public InspiringSong()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.SELF);

        inspiration = baseInspiration = INSPIRATION;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new BuffNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p instanceof Bard) {
            int count = ((Bard) p).noteQueue.count(BuffNote.class);
            if (count > 0) {
                addToBottom(new ApplyPowerAction(p, p, new InspirationPower(p, count, inspiration), count));
            }
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
            count = ((Bard) AbstractDungeon.player).noteQueue.count(BuffNote.class);
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
            upgradeInspiration(UPGRADE_INSPIRATION);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new InspiringSong();
    }
}
