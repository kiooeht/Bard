package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.powers.interfaces.ModifyInspirationPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ModifyInspirationPatch
{
    @SpirePatch(
            clz=AbstractCard.class,
            method="applyPowers"
    )
    public static class ApplyPowers
    {
        public static void Prefix(AbstractCard __instance)
        {
            if (__instance instanceof AbstractBardCard) {
                AbstractBardCard bcard = (AbstractBardCard) __instance;
                bcard.isInspirationModified = false;
                float tmp = bcard.baseInspiration;
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    if (p instanceof ModifyInspirationPower) {
                        tmp = ((ModifyInspirationPower) p).modifyInspiration(tmp);
                        if (bcard.baseInspiration != MathUtils.floor(tmp)) {
                            bcard.isInspirationModified = true;
                        }
                    }
                }
                if (tmp < 0) {
                    tmp = 0;
                }
                bcard.inspiration = MathUtils.floor(tmp);
            }
        }
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="calculateCardDamage"
    )
    public static class CalculateCardDamage
    {
        public static void Prefix(AbstractCard __instance, AbstractMonster mo)
        {
            ApplyPowers.Prefix(__instance);
        }
    }
}
