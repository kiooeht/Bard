package com.evacipated.cardcrawl.mod.bard.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.bard.powers.CovetousAuraPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class CovetousAuraPatch
{
    @SpirePatch(
            clz=ApplyPowerAction.class,
            method="update"
    )
    public static class AppplyPowerActionPatch
    {
        public static void Prefix(ApplyPowerAction __instance)
        {
            float duration = (float) ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
            float startDuration = (float) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "startingDuration");
            if (__instance.target != null && !__instance.target.isDeadOrEscaped() && duration == startDuration) {
                if (!__instance.target.isPlayer && AbstractDungeon.player.hasPower(CovetousAuraPower.POWER_ID)) {
                    AbstractPower powerToApply = (AbstractPower) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");

                    if (powerToApply.ID.equals(StrengthPower.POWER_ID) && powerToApply.amount > 0) {
                        AbstractPlayer p = AbstractDungeon.player;
                        p.getPower(CovetousAuraPower.POWER_ID).flash();
                        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, powerToApply.amount), powerToApply.amount));
                    }
                }
            }
        }
    }
}
