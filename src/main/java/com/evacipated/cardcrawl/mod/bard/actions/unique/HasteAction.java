package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class HasteAction extends AbstractGameAction
{
    public HasteAction()
    {
        actionType = ActionType.WAIT;
        duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        if (duration == Settings.ACTION_DUR_XFAST && p.hasPower(DexterityPower.POWER_ID)) {
            int dexAmt = p.getPower(DexterityPower.POWER_ID).amount;
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, dexAmt), dexAmt));
        }

        tickDuration();
    }
}
