package com.evacipated.cardcrawl.mod.bard.actions.unique;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.CardIgnore;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.patches.CenterGridCardSelectScreen;
import com.evacipated.cardcrawl.mod.bard.relics.BagPipes;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ShiftingAction extends AbstractGameAction
{
    private boolean pickCard = false;
    private int damage = -1;
    private int block = -1;
    private AbstractMonster target;
    private DamageInfo.DamageType damageTypeForTurn;

    public ShiftingAction(AbstractMonster targetMonster, DamageInfo.DamageType damageType, int damageAmt, int blockAmt)
    {
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.WAIT;
        damage = damageAmt;
        block = blockAmt;
        target = targetMonster;
        damageTypeForTurn = damageType;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_XFAST) {
            pickCard = true;
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.addToTop(new ShiftingChoiceCard("Attack", "Deal !D! damage.", AbstractCard.CardType.ATTACK, damage, -1));
            group.addToTop(new ShiftingChoiceCard("Block", "Gain !B! Block.", AbstractCard.CardType.SKILL, -1, block));
            group.group.get(0).color = AbstractCard.CardColor.RED;
            group.group.get(1).color = AbstractCard.CardColor.GREEN;

            CenterGridCardSelectScreen.centerGridSelect = true;
            AbstractDungeon.gridSelectScreen.open(group, 1, "Choose an Action", false);
        } else if (pickCard && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            pickCard = false;
            AbstractCard cardChoice = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            CenterGridCardSelectScreen.centerGridSelect = false;

            if (cardChoice.baseBlock >= 0) {
                AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, cardChoice.baseBlock));
            }
            if (cardChoice.baseDamage >= 0) {
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, cardChoice.baseDamage, damageTypeForTurn)));
            }

            AbstractRelic bagpipes = AbstractDungeon.player.getRelic(BagPipes.ID);
            if (bagpipes != null) {
                bagpipes.onUseCard(cardChoice, null);
            }

            isDone = true;
        }
        tickDuration();
    }

    @CardIgnore
    private static class ShiftingChoiceCard extends AbstractCard
    {
        public static final String ID = BardMod.makeID("ShiftingChoiceCard");
        public static final String IMG = null;
        private static final int COST = -2;

        public ShiftingChoiceCard(String name, String description, CardType type, int damageAmt, int blockAmt)
        {
            super(ID, name, IMG, COST, description, type, Bard.Enums.COLOR, CardRarity.SPECIAL, CardTarget.NONE);

            baseDamage = damageAmt;
            baseBlock = blockAmt;
        }

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
        {

        }

        @Override
        public void upgrade()
        {

        }

        @Override
        public AbstractCard makeCopy()
        {
            return new ShiftingChoiceCard(name, rawDescription, type, baseDamage, baseBlock);
        }
    }
}
