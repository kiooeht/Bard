package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.bard.powers.CursedPower;
import com.evacipated.cardcrawl.mod.bard.vfx.combat.BestowCurseFaceEffect;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;

import java.util.Collections;
import java.util.List;

public class BestowCurse extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("BestowCurse");
    private static final int COST = 2;
    private static final int CURSE_AMT = 4;
    private static final int UPGRADE_CURSE_AMT = 3;
    private static final int EXHAUSTIVE_USES = 2;

    public BestowCurse()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);

        magicNumber = baseMagicNumber = CURSE_AMT;
        ExhaustiveVariable.setBaseValue(this, EXHAUSTIVE_USES);
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(DebuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new VFXAction(new BestowCurseFaceEffect(m.hb.cX, m.hb.cY)));
        addToBottom(new VFXAction(new CollectorCurseEffect(m.hb.cX, m.hb.cY), 1.2f));
        addToBottom(new ApplyPowerAction(m, p, new CursedPower(m, p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_CURSE_AMT);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BestowCurse();
    }
}
