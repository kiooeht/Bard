package com.evacipated.cardcrawl.mod.bard.patches;

import basemod.patches.com.megacrit.cardcrawl.helpers.TipHelper.FakeKeywords;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.relics.BagPipes;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        clz=FakeKeywords.class,
        method="Prefix"
)
public class BagPipesCardNotesPreviewPatch
{
    private static float BODY_TEXT_WIDTH = 0;
    private static float TIP_DESC_LINE_SPACING = 0;
    private static float SHADOW_DIST_Y = 0;
    private static float SHADOW_DIST_X = 0;
    private static float BOX_EDGE_H = 0;
    private static float BOX_BODY_H = 0;
    private static float BOX_W = 0;
    private static float TEXT_OFFSET_X = 0;
    private static float HEADER_OFFSET_Y = 0;
    private static float BODY_OFFSET_Y = 0;
    private static float NOTE_OFFSET_Y = -14 * Settings.scale;

    @SpireInsertPatch(
            locator=Locator1.class,
            localvars={"sumTooltipHeight"}
    )
    public static void Insert1(float x, float[] y, SpriteBatch sb, ArrayList<String> keywords, AbstractCard card, @ByRef float[] sumTooltipHeight)
    {
        if (!BagPipes.hasNotesAvailable() || !BardMod.bagPipeNotesTooltip()) {
            return;
        }

        if (BODY_TEXT_WIDTH == 0) {
            getConstants();
        }

        List<AbstractNote> notes = AbstractBardCard.determineNoteTypes(card);
        if (notes != null && !notes.isEmpty()) {
            float h = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, "test", BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING) - 7.0f * Settings.scale;

            // h is negative, sumTooltipHeight is positive
            sumTooltipHeight[0] += -h;
        }
    }

    @SpireInsertPatch(
            locator=Locator2.class,
            localvars={"sumTooltipHeight"}
    )
    public static void Insert2(float x, float[] y, SpriteBatch sb, ArrayList<String> keywords, AbstractCard card, float sumTooltipHeight)
    {
        if (!BagPipes.hasNotesAvailable() || !BardMod.bagPipeNotesTooltip()) {
            return;
        }

        if (BODY_TEXT_WIDTH == 0) {
            getConstants();
        }

        List<AbstractNote> notes = AbstractBardCard.determineNoteTypes(card);
        if (notes != null && !notes.isEmpty()) {
            float h = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, "test", BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING) - 7.0f * Settings.scale;

            if (sumTooltipHeight > AbstractCard.IMG_HEIGHT) {
                y[0] += h + BOX_EDGE_H * 3.15f;
            }
            renderNoteBox(x, y[0], sb, h, notes);
            y[0] -= h + BOX_EDGE_H * 3.15f;
        }
    }

    private static class Locator1 extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "IMG_HEIGHT");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static class Locator2 extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "size");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static void renderNoteBox(float x, float y, SpriteBatch sb, float h, List<AbstractNote> notes)
    {
        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);

        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, x, y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);

        FontHelper.renderFontLeftTopAligned(
                sb,
                FontHelper.tipHeaderFont,
                BagPipes.NAME,
                x + TEXT_OFFSET_X,
                y + HEADER_OFFSET_Y,
                Settings.GOLD_COLOR
        );

        ShaderProgram oldShader = sb.getShader();
        sb.setShader(BardMod.colorTintShader);
        for (int i=0; i<notes.size(); ++i) {
            BardMod.colorTintShader.setUniformf("tint", notes.get(i).color());
            TipHelper.renderTipEnergy(
                    sb,
                    notes.get(i).getTexture(),
                    x + TEXT_OFFSET_X + (i * (notes.get(i).getTexture().getRegionWidth() + 8) * Settings.scale),
                    y + BODY_OFFSET_Y + NOTE_OFFSET_Y
            );
            sb.flush();
        }
        sb.setShader(oldShader);
    }

    private static void getConstants()
    {
        try {
            Field f = TipHelper.class.getDeclaredField("BODY_TEXT_WIDTH");
            f.setAccessible(true);
            BODY_TEXT_WIDTH = f.getFloat(null);

            f = TipHelper.class.getDeclaredField("TIP_DESC_LINE_SPACING");
            f.setAccessible(true);
            TIP_DESC_LINE_SPACING = f.getFloat(null);

            f = TipHelper.class.getDeclaredField("BOX_EDGE_H");
            f.setAccessible(true);
            BOX_EDGE_H = f.getFloat(null);

            f = TipHelper.class.getDeclaredField("BOX_BODY_H");
            f.setAccessible(true);
            BOX_BODY_H = f.getFloat(null);

            f = TipHelper.class.getDeclaredField("BOX_W");
            f.setAccessible(true);
            BOX_W = f.getFloat(null);

            f = TipHelper.class.getDeclaredField("SHADOW_DIST_X");
            f.setAccessible(true);
            SHADOW_DIST_X = f.getFloat(null);

            f = TipHelper.class.getDeclaredField("SHADOW_DIST_Y");
            f.setAccessible(true);
            SHADOW_DIST_Y = f.getFloat(null);

            f = TipHelper.class.getDeclaredField("TEXT_OFFSET_X");
            f.setAccessible(true);
            TEXT_OFFSET_X = f.getFloat(null);

            f = TipHelper.class.getDeclaredField("HEADER_OFFSET_Y");
            f.setAccessible(true);
            HEADER_OFFSET_Y = f.getFloat(null);

            f = TipHelper.class.getDeclaredField("BODY_OFFSET_Y");
            f.setAccessible(true);
            BODY_OFFSET_Y = f.getFloat(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
