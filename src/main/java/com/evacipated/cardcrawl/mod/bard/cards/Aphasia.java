package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Arrays;
import java.util.List;

public class Aphasia extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("Aphasia");
    public static final String IMG = null;
    private static final int COST = 2;
    private static final int STRLOSS_LOW = 1;
    private static final int UPGRADE_STRLOSS_LOW = 1;
    private static final int STRLOSS_HIGH = 3;
    private static final int UPGRADE_STRLOSS_HIGH = 1;

    public Aphasia()
    {
        super(ID, IMG, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        exhaust = true;
        magicNumber2 = baseMagicNumber2 = STRLOSS_LOW;
        magicNumber = baseMagicNumber = STRLOSS_HIGH;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                new DebuffNote(),
                new DebuffNote()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            int strloss = AbstractDungeon.cardRng.random(magicNumber2, magicNumber);
            addToBottom(new ApplyPowerAction(mo, p, new StrengthPower(mo, -strloss), -strloss));
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber2(UPGRADE_STRLOSS_LOW);
            upgradeMagicNumber(UPGRADE_STRLOSS_HIGH);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Aphasia();
    }
}
