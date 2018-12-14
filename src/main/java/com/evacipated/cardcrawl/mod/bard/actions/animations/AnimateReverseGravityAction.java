package com.evacipated.cardcrawl.mod.bard.actions.animations;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.bard.patches.ReverseGravityAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AnimateReverseGravityAction extends AbstractGameAction
{
    private boolean called = false;

    public AnimateReverseGravityAction()
    {
        startDuration = 1.0f;
        duration = startDuration;
        actionType = ActionType.WAIT;
    }

    @Override
    public void update()
    {
        if (!called) {
            called = true;
            
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                mo.flipVertical = true;
                mo.animX = 0;
                mo.animY = 0;
                //mo.vY = 500 * Settings.scale;
                ReflectionHacks.setPrivate(mo, AbstractCreature.class, "vY", 2500 * Settings.scale);
                //mo.animationTimer = 0.7f;
                ReflectionHacks.setPrivate(mo, AbstractCreature.class, "animationTimer", 0.7f);
                //mo.animation = AbstractCreature.CreatureAnimation.JUMP;
                ReflectionHacks.setPrivate(mo, AbstractCreature.class, "animation", ReverseGravityAnimation.REVERSE_GRAVITY);
            }
        }
        tickDuration();
    }
}
