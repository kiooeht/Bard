package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.relics.Lute;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.lang.reflect.Field;
import java.util.List;

@SpirePatch(
        clz=AbstractCard.class,
        method="renderImage"
)
public class LuteCardNotesPreview2Patch
{
    private static final float NOTE_SPACING = 32;
    private static Texture LUTE_BG;

    public static void Prefix(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected)
    {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(Lute.ID)) {
            return;
        }

        if (LUTE_BG == null) {
            LUTE_BG = ImageMaster.loadImage(BardMod.assetPath("images/cardui/512/card_royal_lute.png"));
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
                    LUTE_BG,
                    __instance.current_x - 256,
                    __instance.current_y - 256,
                    256, 256,
                    512, 512,
                    __instance.drawScale * Settings.scale,
                    __instance.drawScale * Settings.scale,
                    __instance.angle,
                    0, 0,
                    512, 512,
                    false, false
            );

            float offsetX = -(NOTE_SPACING * notes.size() / 2f);
            for (AbstractNote note : notes) {
                Vector2 offset = new Vector2(offsetX, 215);
                offset.rotate(__instance.angle);
                offset.scl(__instance.drawScale * Settings.scale);
                sb.draw(
                        note.getTexture(),
                        __instance.current_x + offset.x,
                        __instance.current_y + offset.y,
                        note.getTexture().packedWidth / 2f,
                        note.getTexture().packedHeight / 2f,
                        note.getTexture().packedWidth,
                        note.getTexture().packedHeight,
                        __instance.drawScale * Settings.scale * 1.5f,
                        __instance.drawScale * Settings.scale * 1.5f,
                        __instance.angle
                );
                offsetX += NOTE_SPACING;
            }
        }
    }
}
