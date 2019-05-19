package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.NoteQueue;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class PlayerNoteQueuePatches
{
    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class NoteQueueField
    {
        public static SpireField<NoteQueue> noteQueue = new SpireField<>(NoteQueue::new);
    }

    @SpirePatch(
            clz=AbstractPlayer.class,
            method="preBattlePrep"
    )
    public static class PreBattlePrep
    {
        public static void Prefix(AbstractPlayer __instance)
        {
            BardMod.getNoteQueue(__instance).reset();
        }
    }

    @SpirePatch(
            clz=AbstractPlayer.class,
            method="render"
    )
    public static class RenderNoteQueue
    {
        public static void Postfix(AbstractPlayer __instance, SpriteBatch sb)
        {
            BardMod.renderNoteQueue(sb, __instance);
        }
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="render"
    )
    public static class RenderMelodiesPanel
    {
        @SpireInsertPatch(
                locator=Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SpriteBatch sb)
        {
            if (AbstractDungeon.rs == AbstractDungeon.RenderScene.NORMAL) {
                BardMod.renderMelodiesPanel(sb, AbstractDungeon.player);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
