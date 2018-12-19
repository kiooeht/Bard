package com.evacipated.cardcrawl.mod.bard.patches;

import com.evacipated.cardcrawl.mod.bard.powers.GreaterMagicWeaponPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

@SpirePatch(
        clz=AbstractMonster.class,
        method="damage"
)
public class GreaterMagicWeaponPatch
{
    public static void Prefix(AbstractMonster __instance, DamageInfo info)
    {
        AbstractPower power = AbstractDungeon.player.getPower(GreaterMagicWeaponPower.POWER_ID);
        if (power instanceof GreaterMagicWeaponPower) {
            ((GreaterMagicWeaponPower) power).beforeUse(info);
        }
    }

    public static void Postfix(AbstractMonster __instance, DamageInfo info)
    {
        AbstractPower power = AbstractDungeon.player.getPower(GreaterMagicWeaponPower.POWER_ID);
        if (power instanceof GreaterMagicWeaponPower) {
            ((GreaterMagicWeaponPower) power).afterUse(info);
        }
    }
}
