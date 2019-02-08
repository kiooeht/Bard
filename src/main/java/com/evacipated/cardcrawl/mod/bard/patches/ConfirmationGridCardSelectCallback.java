package com.evacipated.cardcrawl.mod.bard.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.util.function.Consumer;

@SpirePatch(
        clz=GridCardSelectScreen.class,
        method="update"
)
public class ConfirmationGridCardSelectCallback
{
    public static Consumer<CardGroup> callback = null;

    @SpireInsertPatch(
            locator=Locator.class
    )
    public static SpireReturn<Void> Insert(GridCardSelectScreen __instance)
    {
        if (callback == null) {
            return SpireReturn.Continue();
        }

        try {
            Field targetGroup = GridCardSelectScreen.class.getDeclaredField("targetGroup");
            targetGroup.setAccessible(true);
            callback.accept((CardGroup) targetGroup.get(__instance));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        AbstractDungeon.closeCurrentScreen();

        return SpireReturn.Return(null);
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(GridCardSelectScreen.class, "targetGroup");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
