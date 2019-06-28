package com.evacipated.cardcrawl.mod.bard.cards;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import java.util.Collections;
import java.util.List;

public class PowerWordStun extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("PowerWordStun");
    private static final int COST = 2;
    private static final int STUN = 1;
    private static final int HP_THRESHOLD = 50;
    private static final int UPGRADE_HP_THRESHOLD = 20;

    private CardStrings dootStrings = CardCrawlGame.languagePack.getCardStrings(Doot.ID);

    public PowerWordStun()
    {
        super(ID, COST, CardType.SKILL, Bard.Enums.COLOR, CardRarity.RARE, CardTarget.ENEMY);

        magicNumber = baseMagicNumber = STUN;
        magicNumber2 = baseMagicNumber2 = HP_THRESHOLD;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.singletonList(DebuffNote.get());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBottom(new AbstractGameAction()
        {
            private int stunAmt = magicNumber;
            private float percentThreshold = magicNumber2 / 100.0f;

            @Override
            public void update()
            {
                if ((float) m.currentHealth / m.maxHealth < percentThreshold) {
                    AbstractDungeon.actionManager.addToTop(new StunMonsterAction(m, p, stunAmt));
                    AbstractDungeon.actionManager.addToTop(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.WHITE.cpy(), ShockWaveEffect.ShockWaveType.NORMAL), Settings.FAST_MODE ? 0.3f : 1.5f));
                    AbstractDungeon.actionManager.addToTop(new TalkAction(true, "@" + dootStrings.NAME + "@", 1f, 2f));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        if ((float) mo.currentHealth / mo.maxHealth < magicNumber2 / 100.0f) {
            rawDescription = EXTENDED_DESCRIPTION[0];
        } else {
            rawDescription = EXTENDED_DESCRIPTION[1];
        }
        rawDescription += DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard()
    {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber2(UPGRADE_HP_THRESHOLD);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new PowerWordStun();
    }
}
