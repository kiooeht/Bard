package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;

public class GreaterMagicWeaponPower extends AbstractBardPower
{
    public static final String POWER_ID = BardMod.makeID("GreaterMagicWeapon");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean usedThisTurn = false;

    public GreaterMagicWeaponPower(AbstractCreature owner, int amount)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        loadRegion("greaterMagicWeapon");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        if (!usedThisTurn && card.type == AbstractCard.CardType.ATTACK) {
            usedThisTurn = true;
            ArrayList<AbstractMonster> monsters;
            if (m != null) {
                monsters = new ArrayList<>();
                monsters.add(m);
            } else {
                monsters = AbstractDungeon.getMonsters().monsters;
            }
            int a = amount;
            for (AbstractMonster mon : monsters) {
                // Delays the vulnerable until after the card's effects
                AbstractDungeon.actionManager.addToBottom(
                        new AbstractGameAction()
                        {
                            @Override
                            public void update()
                            {
                                AbstractDungeon.actionManager.addToBottom(
                                        new ApplyPowerAction(mon, AbstractDungeon.player, new VulnerablePower(mon, a, false), a)
                                );
                                isDone = true;
                            }
                        }
                );
            }
        }
    }

    @Override
    public void atStartOfTurn()
    {
        usedThisTurn = false;
    }
}
