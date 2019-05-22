package com.evacipated.cardcrawl.mod.bard.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.city.BronzeOrb;
import javassist.CtBehavior;

@SpirePatch(
        clz=BronzeOrb.class,
        method="update"
)
public class BronzeOrbReverseGravity
{
    @SpireInsertPatch(
            locator=Locator.class
    )
    public static SpireReturn<Void> Insert(BronzeOrb __instance)
    {
        if (ReflectionHacks.getPrivate(__instance, AbstractCreature.class, "animation") == ReverseGravityAnimation.REVERSE_GRAVITY) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception
        {
            Matcher matcher = new Matcher.FieldAccessMatcher(BronzeOrb.class, "count");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
