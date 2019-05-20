package com.evacipated.cardcrawl.mod.bard.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BetterLoseBlockAction extends AbstractGameAction
{
    public BetterLoseBlockAction(AbstractCreature target, AbstractCreature source, int amount)
    {
        setValues(target, source, amount);
        actionType = ActionType.BLOCK;
    }

    @Override
    public void update()
    {
        if (duration == 0.5f) {
            if (target.currentBlock == 0) {
                isDone = true;
                return;
            }

            try {
                Method m = AbstractCreature.class.getDeclaredMethod("decrementBlock", DamageInfo.class, int.class);
                m.setAccessible(true);
                m.invoke(target, new DamageInfo(source, amount, DamageInfo.DamageType.NORMAL), amount);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        tickDuration();
    }
}
