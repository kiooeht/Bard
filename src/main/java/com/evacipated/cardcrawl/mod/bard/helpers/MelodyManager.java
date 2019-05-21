package com.evacipated.cardcrawl.mod.bard.helpers;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.MelodyStrings;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.notes.WildCardNote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MelodyManager
{
    private static List<AbstractNote> allNotes = new ArrayList<>();
    private static Map<String, AbstractNote> notes = new HashMap<>();
    private static List<AbstractMelody> melodies = new ArrayList<>();
    private static Map<String, MelodyStrings> melodyStrings = new HashMap<>();

    public static void addNote(AbstractNote note)
    {
        allNotes.add(note);
        notes.put(note.name() + " Note", note);
        notes.put("[" + note.name() + "Note]", note);
    }

    public static AbstractNote getNoteByAscii(String key)
    {
        for (AbstractNote note : allNotes) {
            if (note.ascii().equals(key)) {
                return note;
            }
        }
        return null;
    }

    public static AbstractNote getNote(String key)
    {
        return notes.get(key);
    }

    public static List<AbstractNote> getAllNotes()
    {
        List<AbstractNote> ret = new ArrayList<>(allNotes);
        ret.removeIf(n -> !n.countsAsNote());
        ret.removeIf(n -> n instanceof WildCardNote);
        return ret;
    }

    public static int getAllNotesCount()
    {
        int count = 0;
        for (AbstractNote note : allNotes) {
            if (note.countsAsNote() && !(note instanceof WildCardNote)) {
                ++count;
            }
        }
        return count;
    }

    public static void addMelody(AbstractMelody melody)
    {
        /*
        for (AbstractMelody m : melodies) {
            if (m.conflictsMelody(melody)) {
                BardMod.logger.error(melody + " conflicts with existing " + m);
                throw new RuntimeException(melody + " conflicts with existing " + m);
            }
        }
        //*/
        melodies.add(melody);
    }

    public static List<AbstractMelody> getAllMelodies()
    {
        return melodies;
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
        List<AbstractMelody> ret = new ArrayList<>();
        for (AbstractMelody melody : melodies) {
            int idx = melody.endIndexOf(notes);
            if (idx != -1) {
                ret.add(melody.makeCopy());
            }
        }
        return ret;
    }

    // MelodyStrings stuff
    public static void loadMelodyStrings(String filepath)
    {
        Gson gson = new Gson();
        Type melodyType = new TypeToken<Map<String, MelodyStrings>>(){}.getType();

        Map<String, MelodyStrings> notes = gson.fromJson(loadJson(BardMod.assetPath("melodies/MelodyNotes.json")), melodyType);
        Map<String, MelodyStrings> map = gson.fromJson(loadJson(filepath), melodyType);

        for (String key : map.keySet()) {
            if (notes.containsKey(key)) {
                map.get(key).NOTES = notes.get(key).NOTES;
            }
        }

        melodyStrings.putAll(map);
    }

    private static String loadJson(String jsonPath)
    {
        return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
    }

    public static MelodyStrings getMelodyStrings(String melodyID)
    {
        return melodyStrings.get(melodyID);
    }
}
