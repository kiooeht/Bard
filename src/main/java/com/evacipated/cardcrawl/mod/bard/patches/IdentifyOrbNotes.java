package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.FontHelper;

@SpirePatch(
        clz=FontHelper.class,
        method="identifyOrb"
)
public class IdentifyOrbNotes
{
    public static TextureAtlas.AtlasRegion Postfix(TextureAtlas.AtlasRegion __result, String word)
    {
        if (__result == null) {
            AbstractNote note = MelodyManager.getNote(word);
            if (note != null) {
                ColorIdentifyOrbNotes.isNote = note;
                return note.getTexture();
            }
        }
        return __result;
    }
}
