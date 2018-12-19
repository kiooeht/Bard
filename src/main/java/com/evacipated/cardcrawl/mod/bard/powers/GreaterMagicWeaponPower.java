package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;

public class GreaterMagicWeaponPower extends AbstractPower
{
    public static final String POWER_ID = BardMod.makeID("GreaterMagicWeapon");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private Map<AbstractMonster, Integer> monsterArmor = new HashMap<>();

    public GreaterMagicWeaponPower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        updateDescription();
        region48 = BardMod.powerAtlas.findRegion("48/greaterMagicWeapon");
        region128 = BardMod.powerAtlas.findRegion("128/greaterMagicWeapon");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0];
    }

    public void beforeUse(DamageInfo info)
    {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                monsterArmor.put(m, m.currentBlock);
                m.currentBlock = 0;
            }
        }
    }

    public void afterUse(DamageInfo info)
    {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (monsterArmor.containsKey(m)) {
                    m.currentBlock = monsterArmor.get(m);
                    monsterArmor.remove(m);
                }
            }
        }
    }
}
