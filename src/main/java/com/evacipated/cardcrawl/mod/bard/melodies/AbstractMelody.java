package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.MelodyStrings;
import com.evacipated.cardcrawl.mod.bard.actions.common.RemoveNoteFromQueueAction;
import com.evacipated.cardcrawl.mod.bard.cards.MelodyCard;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.hooks.OnMelodyPlayedHook;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMelody
{
    protected String name;
    protected String rawDescription;
    protected AbstractCard.CardTarget target;
    protected List<AbstractNote> notes = new ArrayList<>();

    public AbstractMelody(String ID, AbstractCard.CardTarget target)
    {
        MelodyStrings melodyStrings = MelodyManager.getMelodyStrings(ID);
        name = melodyStrings.NAME;
        rawDescription = melodyStrings.DESCRIPTION;
        this.target = target;

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

    public String getName()
    {
        return name;
    }

    public String makeNotesUIString()
    {
        StringBuilder builder = new StringBuilder();
        for (AbstractNote note : notes) {
            builder.append("[").append(note.name()).append("Note] ");
        }
        return builder.toString();
    }

    public AbstractCard makeChoiceCard()
    {
        return new MelodyCard(name, rawDescription, new ArrayList<>(notes), target, this::doPlay);
    }

    public int length()
    {
        return notes.size();
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
            if (!lhs.get(i).getClass().equals(rhs.get(i).getClass())) {
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
            if (AbstractDungeon.player instanceof Bard) {
                int startIndex = ((Bard) AbstractDungeon.player).noteQueueMelodyPosition(this);
                addToBottom(new RemoveNoteFromQueueAction(startIndex, notes.size()));
            }
        }
    }

    public abstract void play();

    public abstract AbstractMelody makeCopy();
}
