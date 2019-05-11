package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.Arrays;
import java.util.List;

public class FaerieFire extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("FaerieFire");
    private static final int COST = 0;
    private static final int VUL = 1;
    private static final int UPGRADE_VUL = 1;

    public FaerieFire()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        magicNumber = baseMagicNumber = VUL;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Arrays.asList(
                DebuffNote.get(),
                DebuffNote.get()
        );
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            addToBottom(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false), magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_VUL);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new FaerieFire();
    }
}
