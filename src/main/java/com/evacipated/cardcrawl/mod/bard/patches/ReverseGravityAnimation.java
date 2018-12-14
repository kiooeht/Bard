package com.evacipated.cardcrawl.mod.bard.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

import java.lang.reflect.Field;

@SpirePatch(
        clz=AbstractCreature.class,
        method="updateAnimations"
)
public class ReverseGravityAnimation
{
    @SpireEnum
    public static AbstractCreature.CreatureAnimation REVERSE_GRAVITY;

    public static void Prefix(AbstractCreature __instance)
    {
        float animationTimer = 0;
        AbstractCreature.CreatureAnimation animation = null;

        try {
            Field f = AbstractCreature.class.getDeclaredField("animationTimer");
            f.setAccessible(true);
            animationTimer = f.getFloat(__instance);

            f = AbstractCreature.class.getDeclaredField("animation");
            f.setAccessible(true);
            animation = (AbstractCreature.CreatureAnimation) f.get(__instance);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        if (animationTimer != 0) {
            if (animation == REVERSE_GRAVITY) {
                float vY = 0;
                try {
                    Field f = AbstractCreature.class.getDeclaredField("vY");
                    f.setAccessible(true);
                    vY = f.getFloat(__instance);
                    vY -= 17 * Settings.scale;
                    f.setFloat(__instance, vY);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

                __instance.animY += vY * Gdx.graphics.getDeltaTime();
                if (__instance.animY < 0) {
                    __instance.animY = 0;
                    __instance.flipVertical = false;
                    ReflectionHacks.setPrivate(__instance, AbstractCreature.class, "animationTimer", 0);
                }
            }
        }
    }
}
