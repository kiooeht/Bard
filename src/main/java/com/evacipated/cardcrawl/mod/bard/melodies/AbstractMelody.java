package com.evacipated.cardcrawl.mod.bard.melodies;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.MelodyStrings;
import com.evacipated.cardcrawl.mod.bard.actions.common.RemoveNoteFromQueueAction;
import com.evacipated.cardcrawl.mod.bard.cards.MelodyCard;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.hooks.OnMelodyPlayedHook;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMelody
{
    protected final String id;
    protected String name;
    protected String rawDescription;
    private String description = null;
    protected AbstractCard.CardType type;
    protected AbstractCard.CardTarget target;
    protected List<AbstractNote> notes = new ArrayList<>();

    public AbstractMelody(String ID, AbstractCard.CardTarget target)
    {
        id = ID;
        MelodyStrings melodyStrings = MelodyManager.getMelodyStrings(ID);
        name = melodyStrings.NAME;
        rawDescription = melodyStrings.DESCRIPTION;
        this.target = target;
        type = AbstractCard.CardType.POWER;

        if (melodyStrings.NOTES != null) {
            for (String noteStr : melodyStrings.NOTES) {
                AbstractNote note = MelodyManager.getNoteByAscii(noteStr);
                if (note != null) {
                    notes.add(note);
                } else {
                    throw new RuntimeException("Invalid note: \"" + noteStr + "\"");
                }
            }
        }
    }

    public AbstractMelody(String name, String rawDescription, AbstractCard.CardTarget target)
    {
        // TODO: this ctor shouldn't be used
        id = null;
        this.name = name;
        this.rawDescription = rawDescription;
        this.target = target;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("Melody (");
        for (AbstractNote note : notes) {
            builder.append(note.ascii()).append(", ");
        }
        builder.setLength(builder.length()-2);
        builder.append(")");

        return builder.toString();
    }

    public String getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        if (description == null) {
            StringBuilder builder = new StringBuilder();
            AbstractCard card = makeChoiceCard();
            for (DescriptionLine line : card.description) {
                builder.append(line.text).append(" ");
            }
            builder.setLength(builder.length() - 1);
            description = builder.toString();

            description = description.replaceAll("\\*", "#y");
            description = description.replaceAll("(?<!#y)Inspiration", "#yInspiration");
        }
        return description;
    }

    public String makeNotesUIString()
    {
        StringBuilder builder = new StringBuilder();
        for (AbstractNote note : notes) {
            builder.append("[").append(note.name()).append("Note] ");
        }
        return builder.toString();
    }

    protected CustomCard.RegionName getRegionName()
    {
        // Remove `modid:`
        // ex: `bard:Artifact` -> `Artifact`
        String id2 = id.replaceFirst("^" + BardMod.makeID(""), "");

        return new CustomCard.RegionName(String.format("%s/%s/melody%s", BardMod.ID, type.name().toLowerCase(), id2));
    }

    public AbstractCard makeChoiceCard()
    {

        AbstractCard ret = new MelodyCard(name, getRegionName(), rawDescription, new ArrayList<>(notes), target, this::doPlay);
        ret.type = type;
        return ret;
    }

    public int length()
    {
        return notes.size();
    }

    public int count(Class<? extends AbstractNote> type)
    {
        int count = 0;
        for (AbstractNote note : notes) {
            if (note.isNoteType(type)) {
                ++count;
            }
        }
        return count;
    }

    public boolean conflictsMelody(AbstractMelody other)
    {
        if (notes.size() == other.notes.size()) {
            return matchesNotes(other.notes);
        }

        AbstractMelody small, large;
        if (notes.size() < other.notes.size()) {
            small = this;
            large = other;
        } else {
            small = other;
            large = this;
        }

        for (int i=0; i<large.notes.size() - small.notes.size() + 1; ++i) {
            if (small.matchesNotes(large.notes.subList(i, i+small.notes.size()))) {
                return true;
            }
        }

        return false;
    }

    public boolean matchesNotes(List<AbstractNote> otherNotes)
    {
        if (notes.size() != otherNotes.size()) {
            return false;
        }

        for (int i=0; i<notes.size(); ++i) {
            if (!notes.get(i).getClass().equals(otherNotes.get(i).getClass())) {
                return false;
            }
        }

        return true;
    }

    public boolean fuzzyMatchesNotes(List<AbstractNote> otherNotes)
    {
        return endIndexOf(otherNotes) != -1;
    }

    public int endIndexOf(List<AbstractNote> otherNotes)
    {
        if (otherNotes.size() < notes.size()) {
            return -1;
        }

        for (int i=0; i<otherNotes.size() - notes.size() + 1; ++i) {
            if (notesMatch(notes, otherNotes.subList(i, i+notes.size()))) {
                return i + notes.size();
            }
        }

        return -1;
    }

    private boolean notesMatch(List<AbstractNote> lhs, List<AbstractNote> rhs)
    {
        for (int i=0; i<lhs.size(); ++i) {
            if (!lhs.get(i).equals(rhs.get(i))) {
                return false;
            }
        }
        return true;
    }

    protected void addToTop(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToTop(action);
    }

    protected void addToBottom(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public final void doPlay(boolean consumeNotes)
    {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof OnMelodyPlayedHook) {
                ((OnMelodyPlayedHook) power).onMelodyPlayed(this);
            }
        }
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof OnMelodyPlayedHook) {
                ((OnMelodyPlayedHook) relic).onMelodyPlayed(this);
            }
        }

        play();

        if (consumeNotes) {
            int startIndex = BardMod.getNoteQueue(AbstractDungeon.player).melodyPosition(this);
            addToBottom(new RemoveNoteFromQueueAction(startIndex, notes.size()));
        }
    }

    public abstract void play();

    public abstract AbstractMelody makeCopy();
}
