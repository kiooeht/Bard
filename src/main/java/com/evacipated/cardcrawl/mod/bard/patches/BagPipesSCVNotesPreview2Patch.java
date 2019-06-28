package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.relics.BagPipes;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

import java.util.List;

@SpirePatch(
        clz=SingleCardViewPopup.class,
        method="render"
)
public class BagPipesSCVNotesPreview2Patch
{
    private static final float NOTE_SPACING = 64;
    private static Texture BAGPIPES_BG;

    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"card"}
    )
    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card)
    {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(BagPipes.ID) || !BardMod.bagPipeNotesCardUI()) {
            return;
        }

        if (BAGPIPES_BG == null) {
            BAGPIPES_BG = ImageMaster.loadImage(BardMod.assetPath("images/cardui/1024/card_royal_bagpipes.png"));
        }

        List<AbstractNote> notes = AbstractBardCard.determineNoteTypes(card);
        if (notes != null && !notes.isEmpty()) {
            sb.draw(
                    BAGPIPES_BG,
                    (Settings.WIDTH - BAGPIPES_BG.getWidth() * Settings.scale) / 2f,
                    Settings.HEIGHT - 200 * Settings.scale,
                    0, 0,
                    BAGPIPES_BG.getWidth(), BAGPIPES_BG.getHeight(),
                    Settings.scale,
                    Settings.scale,
                    0,
                    0, 0,
                    BAGPIPES_BG.getWidth(), BAGPIPES_BG.getHeight(),
                    false, false
            );

            Color oldColor = sb.getColor();
            float offsetX = -(NOTE_SPACING * notes.size() / 2f);
            for (AbstractNote note : notes) {
                Vector2 offset = new Vector2(offsetX, 424);
                offset.scl(Settings.scale);
                sb.setColor(note.color());
                sb.draw(
                        note.getTexture(),
                        Settings.WIDTH / 2f + offset.x,
                        Settings.HEIGHT / 2f + offset.y,
                        0,
                        0,
                        note.getTexture().originalWidth,
                        note.getTexture().originalHeight,
                        Settings.scale * 2.2f,
                        Settings.scale * 2.2f,
                        0
                );
                offsetX += NOTE_SPACING;
            }
            sb.setColor(oldColor);
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderCardBack");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
