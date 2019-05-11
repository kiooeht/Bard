package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
    private static final int COST = 1;
    private static final int INSPIRATION = 50;
    private static final int UPGRADE_INSPIRATION = 25;

    public InspiringSong()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.SELF);

        inspiration = baseInspiration = INSPIRATION;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(BuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                int count = BardMod.getNoteQueue(AbstractDungeon.player).count(BuffNote.class);
                if (count > 0) {
                    addToBottom(new ApplyPowerAction(p, p, new InspirationPower(p, count, inspiration), count));
                }
                isDone = true;
            }
        });

        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        int count = BardMod.getNoteQueue(AbstractDungeon.player).count(BuffNote.class);
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
