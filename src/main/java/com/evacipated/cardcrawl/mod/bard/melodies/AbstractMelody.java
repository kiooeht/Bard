package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMelody
{
    protected List<AbstractNote> notes = new ArrayList<>();

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

    public abstract AbstractMelody makeCopy();
}
