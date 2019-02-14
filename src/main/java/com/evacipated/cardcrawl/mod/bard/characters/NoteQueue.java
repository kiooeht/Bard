package com.evacipated.cardcrawl.mod.bard.characters;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.RemoveNoteFromQueueAction;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.hooks.OnNoteQueuedHook;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.relics.SelfPlayingFlute;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.*;
import java.util.function.Predicate;

public class NoteQueue
{
    private int masterMaxNotes = 0;
    private int maxNotes = masterMaxNotes;
    private Deque<AbstractNote> notes = new ArrayDeque<>();

    public int size()
    {
        return (int) notes.stream()
                .filter(AbstractNote::countsAsNote)
                .count();
    }

    public int getMaxNotes()
    {
        return maxNotes;
    }

    public void increaseMaxNotes(int amount)
    {
        maxNotes += amount;
    }

    public void setMasterMaxNotes(int amount)
    {
        masterMaxNotes = amount;
    }

    public Iterator<AbstractNote> iterator()
    {
        return notes.iterator();
    }

    public void clear()
    {
        notes.clear();
    }

    public void reset()
    {
        if (!AbstractDungeon.player.hasRelic(SelfPlayingFlute.ID)) {
            clear();
        }
        maxNotes = masterMaxNotes;
        while (notes.size() > maxNotes) {
            notes.pollFirst();
        }
    }

    public void queue(AbstractNote note)
    {
        if (note.countsAsNote()) {
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof OnNoteQueuedHook) {
                    note = ((OnNoteQueuedHook) power).onNoteQueued(note);
                    if (note == null) {
                        break;
                    }
                }
            }
            if (note != null) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic instanceof OnNoteQueuedHook) {
                        note = ((OnNoteQueuedHook) relic).onNoteQueued(note);
                        if (note == null) {
                            break;
                        }
                    }
                }
            }
        }
        if (note != null) {
            notes.addLast(note);
            while (notes.size() > maxNotes) {
                notes.removeFirst();
            }
        }
    }

    public boolean removeNote(int index)
    {
        if (index < 0 || index >= notes.size()) {
            return false;
        }

        Iterator<AbstractNote> iter = notes.iterator();
        int i = 0;
        while (iter.hasNext()) {
            iter.next();
            if (i == index) {
                iter.remove();
                for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
                    if (action instanceof RemoveNoteFromQueueAction) {
                        ((RemoveNoteFromQueueAction) action).removed(index);
                    }
                }
                return true;
            }
            ++i;
        }
        return false;
    }

    public boolean removeNotesIf(Predicate<? super AbstractNote> pred)
    {
        return notes.removeIf(pred);
    }

    public int count(Class<? extends AbstractNote> type)
    {
        int count = 0;
        for (AbstractNote note : notes) {
            if (type.isInstance(note)) {
                ++count;
            }
        }
        return count;
    }

    public int uniqueCount()
    {
        Set<String> noteSet = new HashSet<>();
        for (AbstractNote note : notes) {
            if (note.countsAsNote()) {
                noteSet.add(note.ascii());
            }
        }
        return noteSet.size();
    }

    public List<String> getNotesForSaving()
    {
        List<String> noteNames = new ArrayList<>();
        for (AbstractNote note : notes) {
            noteNames.add(note.name() + " Note");
        }
        return noteNames;
    }

    public void loadNotes(List<String> noteNames)
    {
        if (noteNames == null) {
            return;
        }

        notes.clear();
        for (String noteName : noteNames) {
            AbstractNote note = MelodyManager.getNote(noteName);
            if (note != null) {
                notes.addLast(note);
            } else {
                BardMod.logger.warn("Failed to find note: " + noteName);
            }
        }
    }

    public int melodyPosition(AbstractMelody melody)
    {
        int endIndex = melody.endIndexOf(new ArrayList<>(notes));
        if (endIndex < 0) {
            return -1;
        }
        endIndex -= melody.length();
        return endIndex;
    }

    public boolean canPlayAnyMelody()
    {
        return !getPlayableMelodies().isEmpty();
    }

    public boolean canPlayMelody(AbstractMelody melody)
    {
        return melody.fuzzyMatchesNotes(new ArrayList<>(notes));
    }

    public List<AbstractMelody> getPlayableMelodies()
    {
        return MelodyManager.getAllMelodiesFromNotes(new ArrayList<>(notes));
    }


}
