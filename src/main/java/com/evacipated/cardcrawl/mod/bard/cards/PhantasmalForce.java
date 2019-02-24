package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Arrays;
import java.util.List;

public class PhantasmalForce extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("PhantasmalForce");
    private static final int COST = 2;
    private static final int TEMP_HP = 7;
    private static final int UPGRADE_TEMP_HP = 2;

    public PhantasmalForce()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.ENEMY);

        magicNumber = baseMagicNumber = TEMP_HP;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                new BlockNote(),
                new AttackNote()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new AddTemporaryHPAction(p, p, magicNumber));
        if (damage >= 0) {
            addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.POISON));
        }
    }

    private void setBaseDamage()
    {
        damage = baseDamage = TempHPField.tempHp.get(AbstractDungeon.player) + magicNumber;
    }

    @Override
    public void applyPowers()
    {
        setBaseDamage();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        setBaseDamage();
        super.calculateCardDamage(mo);
        if (!mo.hasPower(WeakPower.POWER_ID)) {
            damage = baseDamage = -1;
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_TEMP_HP);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new PhantasmalForce();
    }
}
