package com.evacipated.cardcrawl.mod.bard.cards;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BottledSong extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("BottledSong");
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public BottledSong()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.NONE);

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
        addToBottom(new ObtainPotionAction(null) {
            @Override
            public void update()
            {
                if (duration == Settings.ACTION_DUR_XFAST) {
                    List<AbstractNote> notes = new ArrayList<>();
                    BardMod.getNoteQueue(p).iterator().forEachRemaining(notes::add);
                    ReflectionHacks.setPrivate(this, ObtainPotionAction.class, "potion",
                            new com.evacipated.cardcrawl.mod.bard.potions.BottledSong(notes));
                }
                super.update();
            }
        });
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BottledSong();
    }
}
