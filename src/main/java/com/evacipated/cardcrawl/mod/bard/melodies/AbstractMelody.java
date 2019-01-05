package com.evacipated.cardcrawl.mod.bard.melodies;

import com.evacipated.cardcrawl.mod.bard.cards.MelodyCard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMelody
{
    protected String name;
    protected String rawDescription;
    protected AbstractCard.CardTarget target;
    protected List<AbstractNote> notes = new ArrayList<>();

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

    public String makeUIString()
    {
        StringBuilder builder = new StringBuilder();
        for (AbstractNote note : notes) {
            builder.append("[").append(note.name()).append("Note] ");
        }
        builder.append(name);
        return builder.toString();
    }

    public AbstractCard makeChoiceCard()
    {
        return new MelodyCard(name, rawDescription, new ArrayList<>(notes), target, this::play);
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

    public abstract void play();

    public abstract AbstractMelody makeCopy();
}
