package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class PowerWordStun extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("PowerWordStun");
    public static final String IMG = null;
    private static final int COST = 2;
    private static final int STUN = 1;
    private static final int HP_THRESHOLD = 50;
    private static final int UPGRADE_HP_THRESHOLD = 20;

    public PowerWordStun()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.ENEMY);

        magicNumber = baseMagicNumber = STUN;
        magicNumber2 = baseMagicNumber2 = HP_THRESHOLD;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(new DebuffNote());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new AbstractGameAction()
        {
            private int stunAmt = magicNumber;
            private float percentThreshold = magicNumber2 / 100.0f;

            @Override
            public void update()
            {
                if ((float) m.currentHealth / m.maxHealth < percentThreshold) {
                    AbstractDungeon.actionManager.addToTop(new StunMonsterAction(m, p, stunAmt));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        if ((float) mo.currentHealth / mo.maxHealth < magicNumber2 / 100.0f) {
            rawDescription = EXTENDED_DESCRIPTION[0];
        } else {
            rawDescription = EXTENDED_DESCRIPTION[1];
        }
        rawDescription += DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        rawDescription = DESCRIPTION;
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
            upgradeMagicNumber2(UPGRADE_HP_THRESHOLD);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new PowerWordStun();
    }
}
