package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class FocusedAction extends AbstractGameAction
{
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private int inspirationPerEnergy;
    private int additionalInspiration;

    public FocusedAction(boolean freeToPlayOnce, int energyOnUse, int inspirationPerEnergy)
    {
        this(freeToPlayOnce, energyOnUse, inspirationPerEnergy, 0);
    }

    public FocusedAction(boolean freeToPlayOnce, int energyOnUse, int inspirationPerEnergy, int additionalInspiration)
    {
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        this.inspirationPerEnergy = inspirationPerEnergy;
        this.additionalInspiration = additionalInspiration;
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update()
    {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }
        if (AbstractDungeon.player.hasRelic(ChemicalX.ID)) {
            effect += 2;
            AbstractDungeon.player.getRelic(ChemicalX.ID).flash();
        }

        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new InspirationPower(p, 1, effect * inspirationPerEnergy + additionalInspiration), 1));

        if (!freeToPlayOnce) {
            AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
        }
        isDone = true;
    }
}
