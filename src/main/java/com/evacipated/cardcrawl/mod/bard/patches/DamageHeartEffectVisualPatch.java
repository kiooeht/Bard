package com.evacipated.cardcrawl.mod.bard.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.DamageHeartEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

@SpirePatch(
        clz=DamageHeartEffect.class,
        method="loadImage"
)
public class DamageHeartEffectVisualPatch
{
    public static SpireReturn<TextureAtlas.AtlasRegion> Prefix(DamageHeartEffect __instance)
    {
        if (AbstractDungeon.player.chosenClass == Bard.Enums.BARD) {
            AbstractGameAction.AttackEffect effect = (AbstractGameAction.AttackEffect) ReflectionHacks.getPrivate(__instance, DamageHeartEffect.class, "effect");
            FlashAtkImgEffect flash = new FlashAtkImgEffect(0, 0, effect, true);
            return SpireReturn.Return(flash.img);
        }
        return SpireReturn.Continue();
    }
}
