package com.evacipated.cardcrawl.mod.bard.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class CursedPower extends AbstractPower implements HealthBarRenderPower
{
    public static final String POWER_ID = BardMod.makeID("Cursed");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Color barColor = Color.valueOf("a47da4");

    private AbstractCreature source;

    public CursedPower(AbstractCreature owner, AbstractCreature source, int damage)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.source = source;
        type = PowerType.DEBUFF;
        amount = damage;
        updateDescription();
        region48 = BardMod.powerAtlas.findRegion("48/cursed");
        region128 = BardMod.powerAtlas.findRegion("128/cursed");
    }

    @Override
    public void updateDescription()
    {
        if (owner == null || owner.isPlayer) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void atStartOfTurn()
    {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                flashWithoutSound();
                AbstractDungeon.actionManager.addToBottom(new PoisonLoseHpAction(owner, source, amount, AbstractGameAction.AttackEffect.POISON));
            }
        }
    }

    @Override
    public int getHealthBarAmount()
    {
        return amount;
    }

    @Override
    public Color getColor()
    {
        return barColor;
    }
}
