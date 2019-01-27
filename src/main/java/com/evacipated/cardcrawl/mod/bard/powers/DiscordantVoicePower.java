package com.evacipated.cardcrawl.mod.bard.powers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.hooks.OnNoteQueuedHook;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DiscordantVoicePower extends AbstractPower implements OnNoteQueuedHook
{
    public static final String POWER_ID = BardMod.makeID("DiscordantVoice");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DiscordantVoicePower(AbstractCreature owner, int damage)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = damage;
        updateDescription();
        // TODO
        loadRegion("penNib");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractNote onNoteQueued(AbstractNote note)
    {
        if (note instanceof AttackNote || note instanceof DebuffNote) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new DamageRandomEnemyAction(
                            new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.THORNS),
                            AbstractGameAction.AttackEffect.BLUNT_LIGHT
                    )
            );
        }

        return note;
    }
}
