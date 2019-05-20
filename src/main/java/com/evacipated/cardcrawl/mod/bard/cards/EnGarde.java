package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Arrays;
import java.util.List;

public class EnGarde extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("EnGarde");
    private static final int COST = 2;
    private static final int DAMAGE = 7;
    private static final int BLOCK = 7;
    private static final int INSPIRATION_TIMES = 2;
    private static final int UPGRADE_INSPIRATION_TIMES = 1;

    public EnGarde()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);

        baseDamage = DAMAGE;
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = INSPIRATION_TIMES;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                BlockNote.get(),
                AttackNote.get()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new GainBlockAction(p, p, block));
        addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void applyPowers()
    {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power.ID.equals(InspirationPower.POWER_ID) && power instanceof TwoAmountPower) {
                ((TwoAmountPower) power).amount2 *= magicNumber;
            }
        }

        super.applyPowers();

        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power.ID.equals(InspirationPower.POWER_ID) && power instanceof TwoAmountPower) {
                ((TwoAmountPower) power).amount2 /= magicNumber;
            }
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power.ID.equals(InspirationPower.POWER_ID) && power instanceof TwoAmountPower) {
                ((TwoAmountPower) power).amount2 *= magicNumber;
            }
        }

        super.calculateCardDamage(mo);

        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power.ID.equals(InspirationPower.POWER_ID) && power instanceof TwoAmountPower) {
                ((TwoAmountPower) power).amount2 /= magicNumber;
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_INSPIRATION_TIMES);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new EnGarde();
    }
}
