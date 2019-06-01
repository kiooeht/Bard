package com.evacipated.cardcrawl.mod.bard.helpers.input;

import com.badlogic.gdx.Input;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;

public class BardInputActionSet
{
    public static InputAction noteQueue;

    private static final String NOTE_QUEUE_KEY = BardMod.makeID("NOTE_QUEUE");

    public static void load()
    {
        noteQueue = new InputAction(InputActionSet.prefs.getInteger(NOTE_QUEUE_KEY, Input.Keys.Q));
    }

    public static void save()
    {
        InputActionSet.prefs.putInteger(NOTE_QUEUE_KEY, noteQueue.getKey());
    }

    public static void resetToDefaults()
    {
        noteQueue.remap(Input.Keys.Q);
    }
}
