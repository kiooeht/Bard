package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.relics.Lute;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;

@SpirePatch(
        clz=AbstractCard.class,
        method="renderTitle"
)
public class LuteCardNotesPreview2Patch
{
    private static final float NOTE_SPACING = 32;

    public static void Postfix(AbstractCard __instance, SpriteBatch sb)
    {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(Lute.ID)) {
            return;
        }

        List<AbstractNote> notes = AbstractBardCard.determineNoteTypes(__instance);
        if (notes != null && !notes.isEmpty()) {
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
