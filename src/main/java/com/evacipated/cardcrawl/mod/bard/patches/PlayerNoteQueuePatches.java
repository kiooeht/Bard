package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.NoteQueue;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

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
    public static class Render
    {
        public static void Postfix(AbstractPlayer __instance, SpriteBatch sb)
        {
            BardMod.renderNoteQueue(sb, __instance);
        }
    }
}
