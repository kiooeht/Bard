package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.Map;

public class PrismaticSprayAction extends AbstractGameAction
{
    private boolean freeToPlayOnce = false;
    private boolean upgraded = false;
    private int energyOnUse = -1;

    public PrismaticSprayAction(boolean upgraded, boolean freeToPlayOnce, int energyOnUse)
    {
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
        this.energyOnUse = energyOnUse;
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

        for (int i=0; i<effect; ++i) {
            AbstractCard card = getRandomAttackCard().makeCopy();
            if (upgraded) {
                card.modifyCostForCombat(-999);
            } else {
                card.modifyCostForTurn(-999);
            }
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, 1));
        }
        if (!freeToPlayOnce) {
            AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
        }
        isDone = true;
    }

    private AbstractCard getRandomAttackCard()
    {
        ArrayList<String> tmp = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
            if (c.getValue().type == AbstractCard.CardType.ATTACK) {
                tmp.add(c.getKey());
            }
        }
        return CardLibrary.getCard(tmp.get(AbstractDungeon.cardRng.random(0, tmp.size() - 1)));
    }
}
