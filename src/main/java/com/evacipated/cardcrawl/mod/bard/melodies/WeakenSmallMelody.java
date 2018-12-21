package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class WeakenSmallMelody extends AbstractMelody
{
    public WeakenSmallMelody()
    {
        super("Weaken (S)", "Apply 1 Weak to ALL enemies.", AbstractCard.CardTarget.ALL_ENEMY);

        notes.add(new AttackNote());
        notes.add(new DebuffNote());
    }

    @Override
    public void play()
    {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, 1, false), 1, true));
        }
    }

    @Override
    public AbstractCard makeChoiceCard()
    {
        AbstractCard ret = super.makeChoiceCard();
        ret.type = AbstractCard.CardType.SKILL;
        return ret;
    }

    @Override
    public AbstractMelody makeCopy()
    {
        return new WeakenSmallMelody();
    }
}
