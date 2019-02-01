package com.evacipated.cardcrawl.mod.bard.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.List;

public class MelodiesPanel
{
    private boolean show = true;

    public void toggleShow()
    {
        show = !show;
    }

    public void update()
    {

    }

    public void render(SpriteBatch sb, Bard player, List<AbstractNote> notes)
    {
        if (AbstractDungeon.getCurrMapNode() != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !player.isDead
        ) {
            sb.setColor(Color.WHITE);
            TextureAtlas.AtlasRegion tex = BardMod.noteAtlas.findRegion(show ? "toggleOff" : "toggleOn");
            sb.draw(
                    tex,
                    player.drawX - (Bard.NOTE_SPACING * 3 * Settings.scale) - 32 * Settings.scale,
                    (158) * Settings.scale + player.drawY + player.hb_h / 2.0f,
                    0,
                    0,
                    tex.getRegionWidth(),
                    tex.getRegionHeight(),
                    Settings.scale * 2,
                    Settings.scale * 2,
                    0
            );

            if (!show) {
                return;
            }

            FontHelper.renderFontLeftTopAligned(
                    sb,
                    FontHelper.tipHeaderFont,
                    "Melodies",
                    10 * Settings.scale,
                    Settings.HEIGHT - 170 * Settings.scale,
                    Settings.GOLD_COLOR
            );

            StringBuilder body = new StringBuilder();
            for (AbstractMelody melody : MelodyManager.getAllMelodies()) {
                body.append(melody.makeNotesUIString());
                body.append(" NL ");
            }
            body.setLength(body.length() - 4);

            FontHelper.renderSmartText(
                    sb,
                    FontHelper.tipBodyFont,
                    body.toString(),
                    10 * Settings.scale,
                    Settings.HEIGHT - 200 * Settings.scale,
                    280 * Settings.scale,
                    26 * Settings.scale,
                    Settings.CREAM_COLOR
            );

            float y = Settings.HEIGHT - 206 * Settings.scale;
            for (AbstractMelody melody : MelodyManager.getAllMelodies()) {
                Color color = Settings.CREAM_COLOR;
                if (melody.fuzzyMatchesNotes(notes)) {
                    color = Settings.GOLD_COLOR;
                }
                FontHelper.renderFontRightAligned(
                        sb,
                        FontHelper.tipBodyFont,
                        melody.getName(),
                        300 * Settings.scale,
                        y,
                        color
                );
                y -= 26 * Settings.scale;
            }
        }
    }
}
