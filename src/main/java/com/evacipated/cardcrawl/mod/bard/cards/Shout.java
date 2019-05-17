package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Shout extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Shout");
    private static final int COST = 2;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int STR_LOSS = 2;

    public Shout()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);

        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = STR_LOSS;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        if (magicNumber > 0) {
            return Arrays.asList(
                    AttackNote.get(),
                    DebuffNote.get()
            );
        } else {
            return Collections.singletonList(
                    AttackNote.get()
            );
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        if (magicNumber > 0) {
            addToBottom(new ApplyPowerAction(m, p, new StrengthPower(m, -magicNumber), -magicNumber));
            addToBottom(new AbstractGameAction()
            {
                @Override
                public void update()
                {
                    --baseMagicNumber;
                    magicNumber = baseMagicNumber;
                    isDone = true;
                }
            });
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Shout();
    }
}
