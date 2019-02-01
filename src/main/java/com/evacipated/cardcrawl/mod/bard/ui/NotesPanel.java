package com.evacipated.cardcrawl.mod.bard.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.SelectMelodyAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.HitboxListener;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import java.util.Collection;

public class NotesPanel
{
    static final UIStrings performStrings = CardCrawlGame.languagePack.getUIString(BardMod.makeID("Perform"));
    static final float NOTE_SPACING = 32;

    private float noteFloatTimer = 0;

    private Hitbox notesHb;

    public NotesPanel()
    {
        // This size doesn't matter, it's updated in update()
        notesHb = new Hitbox(32, 32);
    }

    public void update(Bard player)
    {
        notesHb.resize(
                64 * Settings.scale
                        + 32 * Settings.scale * player.getMaxNotes(),
                64 * Settings.scale
        );
        notesHb.translate(
                player.drawX - (NOTE_SPACING * 3 * Settings.scale),
                (146) * Settings.scale + player.drawY + player.hb_h / 2.0f
        );

        notesHb.encapsulatedUpdate(new HitboxListener()
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
                if (player.canPlayMelody()) {
                    AbstractDungeon.actionManager.addToBottom(new SelectMelodyAction());
                }
            }
        });
    }

    public void preRender(SpriteBatch sb, Bard player, Collection<AbstractNote> notes)
    {
        if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                || AbstractDungeon.getCurrRoom() instanceof MonsterRoom)
                && !player.isDead
        ) {
            noteFloatTimer += Gdx.graphics.getDeltaTime() * 2;

            boolean canPlay = player.canPlayMelody();

            sb.setColor(Color.WHITE);
            TextureAtlas.AtlasRegion tex = BardMod.noteAtlas.findRegion(canPlay ? "barsGlow" : "bars");
            // Left section of bars
            sb.draw(
                    tex.getTexture(),
                    player.drawX - (NOTE_SPACING * 3 * Settings.scale),
                    (146) * Settings.scale + player.drawY + player.hb_h / 2.0f,
                    0,
                    0,
                    32,
                    32,
                    Settings.scale * 2,
                    Settings.scale * 2,
                    0,
                    tex.getRegionX(),
                    tex.getRegionY(),
                    32,
                    32,
                    false,
                    false
            );
            // Middle (extendable) section of bars
            sb.draw(
                    tex.getTexture(),
                    player.drawX - (NOTE_SPACING * 3 * Settings.scale) + (64 * Settings.scale),
                    (146) * Settings.scale + player.drawY + player.hb_h / 2.0f,
                    0,
                    0,
                    32,
                    32,
                    Settings.scale * (2 + (player.getMaxNotes() - Bard.MAX_NOTES)),
                    Settings.scale * 2,
                    0,
                    tex.getRegionX() + 32,
                    tex.getRegionY(),
                    32,
                    32,
                    false,
                    false
            );
            // Right section of bars
            sb.draw(
                    tex.getTexture(),
                    player.drawX - (NOTE_SPACING * 3 * Settings.scale) + (64 * Settings.scale) + (32 * (2 + (player.getMaxNotes() - Bard.MAX_NOTES)) * Settings.scale),
                    (146) * Settings.scale + player.drawY + player.hb_h / 2.0f,
                    0,
                    0,
                    32,
                    32,
                    Settings.scale * 2,
                    Settings.scale * 2,
                    0,
                    tex.getRegionX() + 64,
                    tex.getRegionY(),
                    32,
                    32,
                    false,
                    false
            );

            sb.setColor(Color.WHITE);
            // Clef
            float offset = 1.5f * (float) Math.sin(noteFloatTimer - 1.2);
            tex = BardMod.noteAtlas.findRegion(canPlay ? "clefTrebleGlow" : "clefTreble");
            sb.draw(
                    tex,
                    player.drawX - (NOTE_SPACING * 3 * Settings.scale) - 16 * Settings.scale,
                    (offset + 146) * Settings.scale + player.drawY + player.hb_h / 2.0f - 16 * Settings.scale,
                    0,
                    0,
                    tex.getRegionWidth(),
                    tex.getRegionHeight(),
                    Settings.scale * 2,
                    Settings.scale * 2,
                    0
            );

            // Notes
            int i = 0;
            for (AbstractNote note : notes) {
                offset = 3 * (float) Math.sin(noteFloatTimer + i*1.2);
                note.render(
                        sb,
                        player.drawX - (NOTE_SPACING * 2 * Settings.scale) + (i * NOTE_SPACING * Settings.scale),
                        (offset + 158) * Settings.scale + player.drawY + player.hb_h / 2.0f
                );
                ++i;
            }
        }
    }

    public void postRender(SpriteBatch sb, Bard player)
    {
        if (notesHb.hovered && !AbstractDungeon.isScreenUp) {
            String body = performStrings.TEXT[1] + player.getMaxNotes() + performStrings.TEXT[2];

            float height = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, body, 280.0F * Settings.scale, 26.0F * Settings.scale);
            height += 74 * Settings.scale; // accounts for header height, box border, and a bit of spacing

            TipHelper.renderGenericTip(
                    notesHb.x,
                    notesHb.y + notesHb.height + height,
                    performStrings.TEXT[0],
                    body
            );
        }

        notesHb.render(sb);
    }
}
