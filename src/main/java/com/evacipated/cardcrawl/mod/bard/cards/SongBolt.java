package com.evacipated.cardcrawl.mod.bard.cards;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.RemoveNoteFromQueueAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.vfx.combat.ThrowNoteEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SongBolt extends AbstractBardCard
{
    public static final String ID = BardMod.makeID("SongBolt");
    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 2;

    public SongBolt()
    {
        super(ID, COST, CardType.ATTACK, Bard.Enums.COLOR, CardRarity.COMMON, CardTarget.ENEMY);

        baseDamage = DAMAGE;
    }

    @Override
    public List<AbstractNote> getNotes()
    {
        return Collections.emptyList();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {

        float stagger = 0;
        Iterator<AbstractNote> iter = BardMod.getNoteQueue(p).iterator();
        int index = 0;
        while (iter.hasNext()) {
            // Clear queue one at a time to look better
            addToBottom(new RemoveNoteFromQueueAction(index, 1));
            ++index;

            AbstractNote note = iter.next();
            if (note.countsAsNote()) {
                addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
                AbstractDungeon.effectList.add(new ThrowNoteEffect(note, BardMod.notesPanel.getX(), BardMod.notesPanel.getY(), m.hb.cX, m.hb.cY, stagger));
                stagger += Settings.FAST_MODE ? 0.05f : 0.15f;
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SongBolt();
    }
}
