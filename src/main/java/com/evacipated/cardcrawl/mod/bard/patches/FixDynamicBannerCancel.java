package com.evacipated.cardcrawl.mod.bard.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz=AbstractDungeon.class,
        method="genericScreenOverlayReset"
)
public class FixDynamicBannerCancel
{
    public static void Prefix()
    {
        AbstractDungeon.dynamicBanner.hide();
    }
}
