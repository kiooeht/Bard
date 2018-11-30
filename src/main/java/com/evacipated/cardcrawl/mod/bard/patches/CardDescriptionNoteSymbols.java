package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CardDescriptionNoteSymbols
{
    private static Pattern r = Pattern.compile("\\{(.+)\\} ");

    @SpirePatch(
            clz=AbstractCard.class,
            method="renderDescription"
    )
    public static class AlterTmp
    {
        private static String savedTmp = null;

        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"tmp"}
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef String[] tmp)
        {
            if (tmp[0].length() <= 0) {
                return;
            }

            if (savedTmp != null) {
                savedTmp += tmp[0];
                // length-2 to account for space(' ') appended to tmp
                if (tmp[0].length() > 1 && tmp[0].charAt(tmp[0].length()-2) == '}') {
                    tmp[0] = savedTmp;
                    savedTmp = null;
                } else {
                    tmp[0] = "";
                }
            } else if (tmp[0].charAt(0) == '{') {
                if (tmp[0].length() > 1 && tmp[0].charAt(tmp[0].length()-2) == '}') {
                    return;
                }
                savedTmp = tmp[0];
                tmp[0] = "";
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(String.class, "charAt");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="renderDescription"
    )
    public static class RenderSmallEnergyOrb
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 24.0f * Settings.scale;

        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"spacing", "i", "start_x", "draw_y", "font", "textColor", "tmp", "gl"}
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, float spacing, int i, @ByRef float[] start_x, float draw_y,
                                  BitmapFont font, Color textColor, @ByRef String[] tmp, GlyphLayout gl)
        {
            java.util.regex.Matcher m = r.matcher(tmp[0]);
            if (m.find()) {
                gl.width = CARD_ENERGY_IMG_WIDTH * __instance.drawScale;
                AbstractNote note = MelodyManager.getNote(m.group(1));
                if (note != null) {
                    __instance.renderSmallEnergy(sb, note.getTexture(),
                            (start_x[0] - __instance.current_x) / Settings.scale / __instance.drawScale,
                            -100.0f - ((__instance.description.size() - 4.0f) / 2.0f - i + 1.0f) * spacing);
                } else {
                    System.out.println(m.group(1));
                }
                start_x[0] += gl.width;
                tmp[0] = "";
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                int[] lines = LineFinder.findAllInOrder(ctBehavior, new ArrayList<>(), matcher);
                return new int[]{lines[lines.length-1]}; // Only last occurrence
            }
        }
    }

    @SpirePatch(
            clz=AbstractCard.class,
            method="initializeDescription"
    )
    public static class AlterNoteDescriptionSize
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 16.0f * Settings.scale;

        private static String savedWord = null;

        @SpireInsertPatch(
                rloc=94,
                localvars={"gl", "word", "lastChar"}
        )
        public static void Insert(AbstractCard __instance,  @ByRef GlyphLayout[] gl, @ByRef String[] word, StringBuilder lastChar)
        {
            if (savedWord != null) {
                savedWord += word[0];
                gl[0] = new GlyphLayout(FontHelper.cardDescFont_N, " ");
                gl[0].width = CARD_ENERGY_IMG_WIDTH;
                if (lastChar.toString().charAt(0) == '}') {
                    word[0] = savedWord;
                    savedWord = null;
                } else {
                    savedWord += lastChar.toString();
                    word[0] = "";
                }
            } else if (word[0].length() > 0 && word[0].charAt(0) == '{') {
                gl[0] = new GlyphLayout(FontHelper.cardDescFont_N, " ");
                gl[0].width = CARD_ENERGY_IMG_WIDTH;
                if (word[0].length() > 1 && word[0].charAt(word[0].length()-2) == '}') {
                    word[0] = "";
                    lastChar.setLength(0);
                    return;
                }
                savedWord = word[0] + lastChar.toString();
                word[0] = "";
                lastChar.setLength(0);
            }
        }
    }
}
