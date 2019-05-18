package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.AlwaysApplyPowerAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class StrangeHarp extends AbstractBardRelic
{
    public static final String ID = BardMod.makeID("StrangeHarp");

    public StrangeHarp()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK, Bard.Enums.COLOR);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onMonsterDeath(AbstractMonster m)
    {
        int weakAmount = 0;
        int vulnAmount = 0;

        if (m.hasPower(WeakPower.POWER_ID)) {
            weakAmount = m.getPower(WeakPower.POWER_ID).amount;
        }
        if (m.hasPower(VulnerablePower.POWER_ID)) {
            vulnAmount = m.getPower(VulnerablePower.POWER_ID).amount;
        }

        if ((weakAmount > 0 || vulnAmount > 0) && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flash();

            addToBottom(new RelicAboveCreatureAction(m, this));
            AbstractMonster target = AbstractDungeon.getRandomMonster();
            if (target != null) {
                if (weakAmount > 0) {
                    addToBottom(new AlwaysApplyPowerAction(target, m, new WeakPower(target, weakAmount, false), weakAmount));
                }
                if (vulnAmount > 0) {
                    addToBottom(new AlwaysApplyPowerAction(target, m, new VulnerablePower(target, vulnAmount, false), vulnAmount));
                }
            }
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new StrangeHarp();
    }
}
