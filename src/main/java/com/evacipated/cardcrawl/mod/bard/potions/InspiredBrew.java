package com.evacipated.cardcrawl.mod.bard.potions;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class InspiredBrew extends AbstractPotion
{
    public static final String POTION_ID = BardMod.makeID("InspiredBrew");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public InspiredBrew()
    {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.BOLT, PotionColor.BLUE);
        isThrown = false;
    }

    @Override
    public void initializeData()
    {
        potency = getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(
                BaseMod.getKeywordTitle(BardMod.makeID("inspiration")),
                BaseMod.getKeywordDescription(BardMod.makeID("inspiration"))
        ));
    }

    @Override
    public void use(AbstractCreature target)
    {
        target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new InspirationPower(target, 1, potency), 1));
        }
    }

    @Override
    public int getPotency(int ascensionLevel)
    {
        return 100;
    }

    @Override
    public AbstractPotion makeCopy()
    {
        return new InspiredBrew();
    }
}
