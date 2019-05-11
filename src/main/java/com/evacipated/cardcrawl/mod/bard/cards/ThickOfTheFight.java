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

    private int currentMonsterCount = 0;

    public ThickOfTheFight()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        baseDamage = DAMAGE;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                AttackNote.get(),
                AttackNote.get()
        );
    }

    public void monstersChanged(int monsterCount)
    {
        updateCost(currentMonsterCount - monsterCount);
        currentMonsterCount = monsterCount;
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

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        ThickOfTheFight tmp = (ThickOfTheFight) super.makeStatEquivalentCopy();
        tmp.currentMonsterCount = currentMonsterCount;
        return tmp;
    }
}
