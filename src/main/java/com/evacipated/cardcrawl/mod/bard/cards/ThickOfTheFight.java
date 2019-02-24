package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.unique.ThickOfTheFightAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.List;

public class ThickOfTheFight extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("ThickOfTheFight");
    private static final int COST = 3;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_DAMAGE = 3;

    public ThickOfTheFight()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        baseDamage = DAMAGE;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                new AttackNote(),
                new AttackNote()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int livingMonsters = 0;
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                ++livingMonsters;
            }
        }

        addToBottom(new ThickOfTheFightAction(
                AbstractDungeon.getMonsters().getRandomMonster(true),
                new DamageInfo(p, baseDamage),
                livingMonsters
        ));
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        // TODO deal with other sources of cost change
        int diff = cost - costForTurn;
        int newCost = COST;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                --newCost;
            }
        }
        if (newCost < 0) {
            newCost = 0;
        }
        if (newCost != cost) {
            isCostModified = true;
            cost = newCost;
            costForTurn = cost - diff;
            if (costForTurn < 0) {
                costForTurn = 0;
            }
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
        return new ThickOfTheFight();
    }
}
