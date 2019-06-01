package com.evacipated.cardcrawl.mod.bard.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.NoteQueue;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.HitboxListener;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.List;

public class MelodiesPanel
{
    private static final int Y_POS = 220;

    private boolean show = true;

    private Hitbox melodiesToggleHb;
    private MelodiesHitboxListener melodiesToggleHbListener;
    private List<Hitbox> melodyHbs;
    private Hitbox extraMelodiesHb;

    public MelodiesPanel()
    {
        // This size doesn't matter, it's updated in update()
        melodiesToggleHb = new Hitbox(32, 32);

        melodiesToggleHbListener = new MelodiesHitboxListener();

        melodyHbs = new ArrayList<>();
        float y = Settings.HEIGHT - (Y_POS + 52) * Settings.scale;
        for (int i=0; i<MelodyManager.getAllMelodies().size(); ++i) {
            Hitbox hb = new Hitbox(310 * Settings.scale, 26 * Settings.scale);
            hb.translate(0, y);
            melodyHbs.add(hb);

            y -= 26 * Settings.scale;
        }

        extraMelodiesHb = new Hitbox(310 * Settings.scale, 26 * Settings.scale);
    }

    public void toggleShow()
    {
        show = !show;
    }

    public void update(AbstractPlayer player)
    {
        NoteQueue noteQueue = BardMod.getNoteQueue(player);
        if (noteQueue.getMaxNotes() == 0) {
            return;
        }

        melodiesToggleHb.resize(
                48 * Settings.scale,
                64 * Settings.scale
        );
        melodiesToggleHb.translate(
                player.drawX - (NotesPanel.NOTE_SPACING * 3 * Settings.scale) - 48 * Settings.scale,
                BardMod.notesPanel.yOffset * Settings.scale + player.drawY + player.hb_h / 2.0f
        );

        melodiesToggleHb.encapsulatedUpdate(melodiesToggleHbListener);

        int skipped = 0;
        List<AbstractMelody> allMelodies = MelodyManager.getAllMelodies();
        float y = Settings.HEIGHT - (Y_POS + 52) * Settings.scale;
        for (int i=0; i<allMelodies.size(); ++i) {
            if (allMelodies.get(i).length() <= noteQueue.getMaxNotes()) {
                melodyHbs.get(i).translate(0, y);
                melodyHbs.get(i).update();
                y -= 26 * Settings.scale;
            } else {
                ++skipped;
            }
        }
        if (skipped > 0) {
            extraMelodiesHb.translate(0, y);
            extraMelodiesHb.update();
        }
    }

    public void render(SpriteBatch sb, AbstractPlayer player)
    {
        if (AbstractDungeon.getCurrMapNode() != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !player.isDead
        ) {
            NoteQueue noteQueue = BardMod.getNoteQueue(player);
            if (noteQueue.getMaxNotes() == 0) {
                return;
            }

            sb.setColor(Color.WHITE);
            TextureAtlas.AtlasRegion tex = BardMod.noteAtlas.findRegion(show ? "toggleOff" : "toggleOn");
            sb.draw(
                    tex,
                    player.drawX - (NotesPanel.NOTE_SPACING * 3 * Settings.scale) - 32 * Settings.scale,
                    (BardMod.notesPanel.yOffset + NotesPanel.EXTRA_OFFSET) * Settings.scale + player.drawY + player.hb_h / 2.0f,
                    0,
                    0,
                    tex.getRegionWidth(),
                    tex.getRegionHeight(),
                    Settings.scale * 2,
                    Settings.scale * 2,
                    0
            );

            if (show) {
                FontHelper.renderFontLeftTopAligned(
                        sb,
                        FontHelper.tipHeaderFont,
                        NotesPanel.performStrings.TEXT[4],
                        10 * Settings.scale,
                        Settings.HEIGHT - Y_POS * Settings.scale,
                        Settings.GOLD_COLOR
                );

                StringBuilder body = new StringBuilder();
                for (AbstractMelody melody : MelodyManager.getAllMelodies()) {
                    if (melody.length() <= noteQueue.getMaxNotes()) {
                        body.append(melody.makeNotesUIString());
                        body.append(" NL ");
                    }
                }
                body.setLength(body.length() - 4);

                FontHelper.renderSmartText(
                        sb,
                        FontHelper.tipBodyFont,
                        body.toString(),
                        10 * Settings.scale,
                        Settings.HEIGHT - (Y_POS + 30) * Settings.scale,
                        280 * Settings.scale,
                        26 * Settings.scale,
                        Settings.CREAM_COLOR
                );

                int skipped = 0;
                float y = Settings.HEIGHT - (Y_POS + 36) * Settings.scale;
                for (AbstractMelody melody : MelodyManager.getAllMelodies()) {
                    if (melody.length() <= noteQueue.getMaxNotes()) {
                        Color color = Settings.CREAM_COLOR;
                        if (noteQueue.canPlayMelody(melody)) {
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
                    } else {
                        ++skipped;
                    }
                }
                if (skipped > 0) {
                    // "+X more" text
                    FontHelper.renderFontRightAligned(
                            sb,
                            FontHelper.tipBodyFont,
                            String.format(NotesPanel.performStrings.TEXT[5], skipped),
                            300 * Settings.scale,
                            y,
                            Settings.CREAM_COLOR
                    );
                }
            }

            // Toggle tooltip
            if (melodiesToggleHb.hovered && !AbstractDungeon.isScreenUp) {
                float height = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, "", 280.0F * Settings.scale, 26.0F * Settings.scale);
                height += 74 * Settings.scale; // accounts for header height, box border, and a bit of spacing

                TipHelper.renderGenericTip(
                        melodiesToggleHb.x,
                        melodiesToggleHb.y + melodiesToggleHb.height + height,
                        NotesPanel.performStrings.TEXT[3],
                        ""
                );
            }

            melodiesToggleHb.render(sb);

            if (show) {
                // Hitboxes / Melody tooltips
                int skipped = 0;
                List<AbstractMelody> allMelodies = MelodyManager.getAllMelodies();
                for (int i=0; i<melodyHbs.size(); ++i) {
                    if (allMelodies.get(i).length() <= noteQueue.getMaxNotes()) {
                        Hitbox hb = melodyHbs.get(i);
                        if (hb.hovered && !AbstractDungeon.isScreenUp) {
                            TipHelper.renderGenericTip(
                                    hb.x + hb.width,
                                    InputHelper.mY,
                                    allMelodies.get(i).getName(),
                                    allMelodies.get(i).makeNotesUIString() + " NL " + allMelodies.get(i).getDescription()
                            );
                        }

                        hb.render(sb);
                    } else {
                        ++skipped;
                    }
                }
                if (skipped > 0) {
                    if (extraMelodiesHb.hovered && !AbstractDungeon.isScreenUp) {
                        TipHelper.renderGenericTip(
                                extraMelodiesHb.x + extraMelodiesHb.width,
                                InputHelper.mY,
                                NotesPanel.performStrings.TEXT[6],
                                NotesPanel.performStrings.TEXT[7]
                        );
                    }
                    extraMelodiesHb.render(sb);
                }
            }
        }
    }

    private class MelodiesHitboxListener implements HitboxListener
    {
        @Override
        public void hoverStarted(Hitbox hitbox)
        {

        }

        @Override
        public void startClicking(Hitbox hitbox)
        {

        }

        @Override
        public void clicked(Hitbox hitbox)
        {
            toggleShow();
        }
    }
}
