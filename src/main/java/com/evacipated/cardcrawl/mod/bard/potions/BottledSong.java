package com.evacipated.cardcrawl.mod.bard.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BottledSong extends AbstractPotion implements CustomSavable<List<String>>
{
    public static final String POTION_ID = BardMod.makeID("BottledSong");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    private List<AbstractNote> notes = new ArrayList<>();

    private static List<AbstractNote> getRandomNotes(int amount)
    {
        List<AbstractNote> allNotes = MelodyManager.getAllNotes();
        List<AbstractNote> ret = new ArrayList<>();
        for (int i=0; i<amount; ++i) {
            ret.add(allNotes.get(MathUtils.random(allNotes.size() - 1)));
        }
        return ret;
    }

    @Override
    public List<String> onSave()
    {
        List<String> noteNames = new ArrayList<>();
        for (AbstractNote note : notes) {
            noteNames.add(note.name() + " Note");
        }
        return noteNames;
    }

    @Override
    public void onLoad(List<String> noteNames)
    {
        if (noteNames == null) {
            return;
        }

        notes.clear();
        for (String noteName : noteNames) {
            AbstractNote note = MelodyManager.getNote(noteName);
            if (note != null) {
                notes.add(note);
            } else {
                BardMod.logger.warn("Failed to find note: " + noteName);
            }
        }

        updateDescription();
    }

    public BottledSong()
    {
        this(getRandomNotes(2));
    }

    public BottledSong(List<AbstractNote> notes)
    {
        super(NAME, POTION_ID, PotionRarity.PLACEHOLDER, PotionSize.EYE, PotionColor.FAIRY);

        this.notes.addAll(notes);
        potency = getPotency();
        isThrown = false;

        updateDescription();
    }

    private void updateDescription()
    {
        description = DESCRIPTIONS[0];
        description += this.notes.stream()
                .map(AbstractNote::cardCode)
                .collect(Collectors.joining(" "));

        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(
                BaseMod.getKeywordTitle(BardMod.makeID("queue")),
                BaseMod.getKeywordDescription(BardMod.makeID("queue"))
        ));
        tips.add(new PowerTip(
                BaseMod.getKeywordTitle(BardMod.makeID("note")),
                BaseMod.getKeywordDescription(BardMod.makeID("note"))
        ));
    }

    @Override
    public void use(AbstractCreature target)
    {
        target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            for (AbstractNote note : notes) {
                AbstractDungeon.actionManager.addToBottom(new QueueNoteAction(note));
            }
        }
    }

    @Override
    public int getPotency(int ascensionLevel)
    {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy()
    {
        return new BottledSong(notes);
    }
}
