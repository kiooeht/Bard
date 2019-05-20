package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BucklerBashAction extends AbstractGameAction
{
    private int blockLoss;
    private int damage;

    public BucklerBashAction(AbstractCreature target, AbstractCreature source, int blockLoss, int damage, DamageInfo.DamageType damageType)
    {
        setValues(target, source);
        this.blockLoss = blockLoss;
        this.damage = damage;
        this.damageType = damageType;
    }

    @Override
    public void update()
    {
        if (duration == DEFAULT_DURATION) {
            if (source.currentBlock == 0) {
                isDone = true;
                return;
            }

            boolean hadEnoughBlock = source.currentBlock >= blockLoss;

            try {
                Method m = AbstractCreature.class.getDeclaredMethod("decrementBlock", DamageInfo.class, int.class);
                m.setAccessible(true);
                m.invoke(source, new DamageInfo(source, blockLoss, DamageInfo.DamageType.NORMAL), blockLoss);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {
            }

            if (hadEnoughBlock) {
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(source, damage, damageType), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
        tickDuration();
    }
}
