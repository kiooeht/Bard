package com.evacipated.cardcrawl.mod.bard.patches.powerInterfaces;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.bard.powers.interfaces.ModifyBlockFinalPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch(
        clz=AbstractCard.class,
        method="applyPowersToBlock"
)
public class ModifyBlockFinalPowerPatch
{
    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"tmp"}
    )
    public static void Insert(AbstractCard __instance, @ByRef float[] tmp)
    {
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof ModifyBlockFinalPower) {
                tmp[0] = ((ModifyBlockFinalPower) p).modifyBlockFinal(tmp[0]);
                if (__instance.baseBlock != MathUtils.floor(tmp[0])) {
                    __instance.isBlockModified = true;
                }
            }
        }

        if (tmp[0] < 0) {
            tmp[0] = 0;
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{found[found.length-1]};
        }
    }
}
