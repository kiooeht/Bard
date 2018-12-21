package com.evacipated.cardcrawl.mod.bard.helpers;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;

import java.util.*;

public class MelodyManager
{
    private static Map<String, AbstractNote> notes = new HashMap<>();
    private static List<AbstractMelody> melodies = new ArrayList<>();

    public static void addNote(AbstractNote note)
    {
        notes.put(note.name() + " Note", note);
    }

    public static AbstractNote getNote(String key)
    {
        return notes.get(key);
    }

    public static List<AbstractNote> getAllNotes()
    {
        return new ArrayList<>(notes.values());
    }

    public static void addMelody(AbstractMelody melody)
    {
        for (AbstractMelody m : melodies) {
            if (m.conflictsMelody(melody)) {
                BardMod.logger.error(melody + " conflicts with existing " + m);
                throw new RuntimeException(melody + " conflicts with existing " + m);
            }
        }
        melodies.add(melody);
    }

    public static AbstractMelody getMelodyFromNotes(List<AbstractNote> notes)
    {
        for (AbstractMelody melody : melodies) {
            if (melody.matchesNotes(notes)) {
                return melody.makeCopy();
            }
        }
        return null;
    }

    public static List<AbstractMelody> getAllMelodiesFromNotes(List<AbstractNote> notes)
    {
        SortedMap<Integer, AbstractMelody> matches = new TreeMap<>();
        for (AbstractMelody melody : melodies) {
            int idx = melody.endIndexOf(notes);
            if (idx != -1) {
                matches.put(idx, melody.makeCopy());
            }
        }
        return new ArrayList<>(matches.values());
    }
}
