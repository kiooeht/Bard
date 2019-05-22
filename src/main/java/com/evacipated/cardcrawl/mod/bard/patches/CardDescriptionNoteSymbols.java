package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CardDescriptionNoteSymbols
{
    @SpirePatch(
            clz=AbstractCard.class,
            method="renderDescription"
    )
    @SpirePatch(
            clz=AbstractCard.class,
            method="renderDescriptionCN"
    )
    public static class RenderSmallNote
    {
        private static final float CARD_ENERGY_IMG_WIDTH = 24.0f * Settings.scale;

        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"spacing", "i", "start_x", "draw_y", "font", "textColor", "tmp", "gl"}
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb,
                                  float spacing, int i, @ByRef float[] start_x, float draw_y,
                                  BitmapFont font, Color textColor, @ByRef String[] tmp, GlyphLayout gl)
        {
            if (tmp[0].length() > 0 && tmp[0].charAt(0) == '[') {
                AbstractNote note = MelodyManager.getNote(tmp[0].trim());
                if (note != null) {
                    gl.width = CARD_ENERGY_IMG_WIDTH * __instance.drawScale;
                    renderSmallNote(__instance, sb, note.getTexture(),
                            (start_x[0] - __instance.current_x) / Settings.scale / __instance.drawScale,
                            (-98.0f - ((__instance.description.size() - 4.0f) / 2.0f - i + 1.0f) * spacing),
                            note.color());
                    start_x[0] += gl.width;
                    tmp[0] = "";
                }
            }
        }

        public static void renderSmallNote(AbstractCard card, SpriteBatch sb, TextureAtlas.AtlasRegion region, float offsetX, float offsetY, Color color)
        {
            try {
                Field f = AbstractCard.class.getDeclaredField("renderColor");
                f.setAccessible(true);
                Color cardColor = (Color) f.get(card);
                color = color.cpy();
                color.a = cardColor.a;
                sb.setColor(color);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                sb.setColor(color);
            }

            Affine2 aff = new Affine2();
            aff.setToTrnRotScl(
                    card.current_x + offsetX * card.drawScale * Settings.scale,
                    card.current_y + offsetY * card.drawScale * Settings.scale,
                    MathUtils.degreesToRadians * card.angle,
                    card.drawScale * Settings.scale,
                    card.drawScale * Settings.scale
            );
            sb.draw(
                    region,
                    region.packedWidth,
                    region.packedHeight,
                    aff
            );
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                int[] lines = LineFinder.findAllInOrder(ctBehavior, matcher);
                return new int[]{lines[lines.length-1]}; // Only last occurrence
            }
        }
    }

    @SpirePatch(
            clz=SingleCardViewPopup.class,
            method="renderDescription"
    )
    @SpirePatch(
            clz=SingleCardViewPopup.class,
            method="renderDescriptionCN"
    )
    public static class RenderSmallNoteSingleCardView
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={
                        "spacing", "i", "start_x", "tmp", "gl",
                        "card_energy_w", "drawScale", "current_x", "card"
                }
        )
        public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb,
                                  float spacing, int i, @ByRef float[] start_x,
                                  @ByRef String[] tmp, GlyphLayout gl,
                                  float card_energy_w, float drawScale, float current_x, AbstractCard card)
        {
            if (tmp[0].length() > 0 && tmp[0].charAt(0) == '[') {
                AbstractNote note = MelodyManager.getNote(tmp[0].trim());
                if (note != null) {
                    gl.width = card_energy_w * drawScale;
                    Color white = Color.WHITE.cpy();
                    Color oldColor = sb.getColor();
                    try {
                        Method renderSmallEnergy = SingleCardViewPopup.class.getDeclaredMethod("renderSmallEnergy", SpriteBatch.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
                        renderSmallEnergy.setAccessible(true);

                        Color.WHITE.set(note.color());
                        renderSmallEnergy.invoke(__instance, sb, note.getTexture(),
                                (start_x[0] - current_x) / Settings.scale / drawScale,
                                -86.0f - ((card.description.size() - 4.0f) / 2.0f - i + 1.0f) * spacing);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    } finally {
                        Color.WHITE.set(white);
                        sb.setColor(oldColor);
                    }
                    start_x[0] += gl.width;
                    tmp[0] = "";
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                int[] lines = LineFinder.findAllInOrder(ctBehavior, matcher);
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

        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"gl", "word", "lastChar"}
        )
        public static void Insert(AbstractCard __instance,  @ByRef GlyphLayout[] gl, String word, StringBuilder lastChar)
        {
            if (word.length() > 0 && word.charAt(0) == '[') {
                AbstractNote note = MelodyManager.getNote(word.trim());
                if (note != null) {
                    gl[0] = new GlyphLayout(FontHelper.cardDescFont_N, " ");
                    gl[0].width = CARD_ENERGY_IMG_WIDTH;
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "DESC_BOX_WIDTH");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}
