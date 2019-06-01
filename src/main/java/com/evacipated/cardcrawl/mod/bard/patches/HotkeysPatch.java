package com.evacipated.cardcrawl.mod.bard.patches;

import com.evacipated.cardcrawl.mod.bard.helpers.input.BardInputActionSet;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;

public class HotkeysPatch
{
    @SpirePatch(
            clz=InputActionSet.class,
            method="load"
    )
    public static class Load
    {
        public static void Prefix()
        {
            BardInputActionSet.load();
        }
    }

    @SpirePatch(
            clz=InputActionSet.class,
            method="save"
    )
    public static class Save
    {
        public static void Prefix()
        {
            BardInputActionSet.save();
        }
    }

    @SpirePatch(
            clz=InputActionSet.class,
            method="resetToDefaults"
    )
    public static class Reset
    {
        public static void Prefix()
        {
            BardInputActionSet.resetToDefaults();
        }
    }
}
