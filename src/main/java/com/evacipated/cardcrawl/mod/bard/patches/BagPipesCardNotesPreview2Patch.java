package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.relics.BagPipes;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.lang.reflect.Field;
import java.util.List;

@SpirePatch(
        clz=AbstractCard.class,
        method="renderImage"
)
public class BagPipesCardNotesPreview2Patch
{
    private static final float NOTE_SPACING = 32;
    private static Texture BAGPIPES_BG;

    public static void Prefix(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected)
    {
        if (!BagPipes.hasNotesAvailable() || !BardMod.bagPipeNotesCardUI()) {
            return;
        }

        if (BAGPIPES_BG == null) {
            BAGPIPES_BG = ImageMaster.loadImage(BardMod.assetPath("images/cardui/512/card_royal_bagpipes.png"));
        }

        List<AbstractNote> notes = AbstractBardCard.determineNoteTypes(__instance);
        if (notes != null && !notes.isEmpty()) {
            try {
                Field renderColor = AbstractCard.class.getDeclaredField("renderColor");
                renderColor.setAccessible(true);
                sb.setColor((Color) renderColor.get(__instance));
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
            }
            sb.draw(
                    BAGPIPES_BG,
                    __instance.current_x - 256,
                    __instance.current_y - 256,
                    256, 256,
                    512, 768,
                    __instance.drawScale * Settings.scale,
                    __instance.drawScale * Settings.scale,
                    __instance.angle,
                    0, 0,
                    512, 768,
                    false, false
            );

            Color oldColor = sb.getColor();
            float offsetX = -(NOTE_SPACING * notes.size() / 2f);
            for (AbstractNote note : notes) {
                Vector2 offset = new Vector2(offsetX, 212);
                offset.rotate(__instance.angle);
                offset.scl(__instance.drawScale * Settings.scale);
                sb.setColor(note.color());
                sb.draw(
                        note.getTexture(),
                        __instance.current_x + offset.x,
                        __instance.current_y + offset.y,
                        0,
                        0,
                        note.getTexture().originalWidth,
                        note.getTexture().originalHeight,
                        __instance.drawScale * Settings.scale * 1.1f,
                        __instance.drawScale * Settings.scale * 1.1f,
                        __instance.angle
                );
                offsetX += NOTE_SPACING;
            }
            sb.setColor(oldColor);
        }
    }
}
