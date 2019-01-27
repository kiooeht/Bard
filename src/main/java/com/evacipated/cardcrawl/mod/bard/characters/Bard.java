package com.evacipated.cardcrawl.mod.bard.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.SelectMelodyAction;
import com.evacipated.cardcrawl.mod.bard.cards.Defend_Bard;
import com.evacipated.cardcrawl.mod.bard.cards.Inspire;
import com.evacipated.cardcrawl.mod.bard.cards.Riposte;
import com.evacipated.cardcrawl.mod.bard.cards.Strike_Bard;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.powers.interfaces.OnNoteQueuedPower;
import com.evacipated.cardcrawl.mod.bard.relics.Lute;
import com.evacipated.cardcrawl.mod.bard.relics.PitchPipe;
import com.evacipated.cardcrawl.mod.bard.relics.SelfPlayingFlute;
import com.evacipated.cardcrawl.mod.bard.relics.interfaces.OnNoteQueuedRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.*;

public class Bard extends CustomPlayer implements HitboxListener
{
    private static final UIStrings performStrings = CardCrawlGame.languagePack.getUIString(BardMod.makeID("Perform"));

    private static final int START_HP = 70;
    private static final int ENERGY_PER_TURN = 3;
    private static final int START_ORBS = 0;

    private static final int MAX_NOTES = 4;
    private static final float NOTE_SPACING = 32;

    private int maxNotes = MAX_NOTES;
    private Deque<AbstractNote> notes = new ArrayDeque<>();

    private float noteFloatTimer = 0;

    private Hitbox notesHb;

    public static class Enums
    {
        @SpireEnum
        public static AbstractPlayer.PlayerClass BARD;
        @SpireEnum(name="BARD_COLOR")
        public static AbstractCard.CardColor COLOR;
        @SpireEnum(name="BARD_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    public Bard(String name)
    {
        super(name, Enums.BARD, null, null,
                new SpineAnimation("images/characters/theSilent/idle/skeleton.atlas", "images/characters/theSilent/idle/skeleton.json", 1.0F));

        dialogX = drawX + 0 * Settings.scale;
        dialogY = drawY + 170 * Settings.scale;

        initializeClass(null, "images/characters/theSilent/shoulder2.png", "images/characters/theSilent/shoulder.png", "images/characters/theSilent/corpse.png",
                getLoadout(), 0.0F, -20.0F, 240.0F, 240.0F, new EnergyManager(ENERGY_PER_TURN));

        notesHb = new Hitbox(32, 32); // This size doesn't matter, it's updated in update()
    }

    public void increaseMaxNotes(int amount)
    {
        maxNotes += amount;
    }

    public List<String> getNotesForSaving()
    {
        List<String> noteNames = new ArrayList<>();
        for (AbstractNote note : notes) {
            noteNames.add(note.name() + " Note");
        }
        return noteNames;
    }

    public void loadNotes(List<String> noteNames)
    {
        if (noteNames == null) {
            return;
        }

        notes.clear();
        for (String noteName : noteNames) {
            AbstractNote note = MelodyManager.getNote(noteName);
            if (note != null) {
                notes.addLast(note);
            } else {
                BardMod.logger.warn("Failed to find note: " + noteName);
            }
        }
    }

    public void clearNoteQueue()
    {
        notes.clear();
    }

    public boolean removeNoteFromQueue(int index)
    {
        if (index < 0 || index >= notes.size()) {
            return false;
        }

        Iterator<AbstractNote> iter = notes.iterator();
        int i = 0;
        while (iter.hasNext()) {
            iter.next();
            if (i == index) {
                iter.remove();
                return true;
            }
            ++i;
        }
        return false;
    }

    public int noteQueueSize()
    {
        return notes.size();
    }

    public int noteQueueCount(Class<? extends AbstractNote> type)
    {
        int count = 0;
        for (AbstractNote note : notes) {
            if (type.isInstance(note)) {
                ++count;
            }
        }
        return count;
    }

    public int noteQueueMelodyPosition(AbstractMelody melody)
    {
        int endIndex = melody.endIndexOf(new ArrayList<>(notes));
        if (endIndex < 0) {
            return -1;
        }
        endIndex -= melody.length();
        return endIndex;
    }

    public void queueNote(AbstractNote note)
    {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof OnNoteQueuedPower) {
                note = ((OnNoteQueuedPower) power).onNoteQueued(note);
                if (note == null) {
                    break;
                }
            }
        }
        if (note != null) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof OnNoteQueuedRelic) {
                    note = ((OnNoteQueuedRelic) relic).onNoteQueued(note);
                    if (note == null) {
                        break;
                    }
                }
            }
        }
        if (note != null) {
            notes.addLast(note);
            while (notes.size() > maxNotes) {
                notes.removeFirst();
            }
        }
    }

    public boolean canPlayMelody()
    {
        return !getPlayableMelodies().isEmpty();
    }

    public List<AbstractMelody> getPlayableMelodies()
    {
        return MelodyManager.getAllMelodiesFromNotes(new ArrayList<>(notes));
    }

    @Override
    public void preBattlePrep()
    {
        if (!hasRelic(SelfPlayingFlute.ID)) {
            clearNoteQueue();
        }
        maxNotes = MAX_NOTES;
        while (notes.size() > maxNotes) {
            notes.pollFirst();
        }
        super.preBattlePrep();
    }

    @Override
    public void update()
    {
        super.update();

        notesHb.resize(
                64 * Settings.scale
                        + 32 * Settings.scale * maxNotes,
                64 * Settings.scale
        );
        notesHb.translate(
                drawX - (NOTE_SPACING * 3 * Settings.scale),
                (146) * Settings.scale + drawY + hb_h / 2.0f
        );

        notesHb.encapsulatedUpdate(this);
    }

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
        if (canPlayMelody()) {
            AbstractDungeon.actionManager.addToBottom(new SelectMelodyAction());
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        renderNotesQueue(sb);

        super.render(sb);

        renderMelodiesPanel(sb);

        if (notesHb.hovered && !AbstractDungeon.isScreenUp) {
            String body = performStrings.TEXT[1] + maxNotes + performStrings.TEXT[2];

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

    private void renderNotesQueue(SpriteBatch sb)
    {
        if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                || AbstractDungeon.getCurrRoom() instanceof MonsterRoom)
                && !isDead
        ) {
            noteFloatTimer += Gdx.graphics.getDeltaTime() * 2;

            boolean canPlay = canPlayMelody();

            sb.setColor(Color.WHITE);
            TextureAtlas.AtlasRegion tex = BardMod.noteAtlas.findRegion(canPlay ? "barsGlow" : "bars");
            // Left section of bars
            sb.draw(
                    tex.getTexture(),
                    drawX - (NOTE_SPACING * 3 * Settings.scale),
                    (146) * Settings.scale + drawY + hb_h / 2.0f,
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
                    drawX - (NOTE_SPACING * 3 * Settings.scale) + (64 * Settings.scale),
                    (146) * Settings.scale + drawY + hb_h / 2.0f,
                    0,
                    0,
                    32,
                    32,
                    Settings.scale * (2 + (maxNotes - MAX_NOTES)),
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
                    drawX - (NOTE_SPACING * 3 * Settings.scale) + (64 * Settings.scale) + (32 * (2 + (maxNotes - MAX_NOTES)) * Settings.scale),
                    (146) * Settings.scale + drawY + hb_h / 2.0f,
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
                    drawX - (NOTE_SPACING * 3 * Settings.scale) - 16 * Settings.scale,
                    (offset + 146) * Settings.scale + drawY + hb_h / 2.0f - 16 * Settings.scale,
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
                        drawX - (NOTE_SPACING * 2 * Settings.scale) + (i * NOTE_SPACING * Settings.scale),
                        (offset + 158) * Settings.scale + drawY + hb_h / 2.0f
                );
                ++i;
            }
        }
    }

    private void renderMelodiesPanel(SpriteBatch sb)
    {
        if (AbstractDungeon.getCurrMapNode() != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !isDead
        ) {
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
                if (melody.fuzzyMatchesNotes(new ArrayList<>(notes))) {
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

    @Override
    public ArrayList<String> getStartingDeck()
    {
        ArrayList<String> ret = new ArrayList<>();
        //*
        // 4 strikes
        ret.add(Strike_Bard.ID);
        ret.add(Strike_Bard.ID);
        ret.add(Strike_Bard.ID);
        ret.add(Strike_Bard.ID);
        // 4 defends
        ret.add(Defend_Bard.ID);
        ret.add(Defend_Bard.ID);
        ret.add(Defend_Bard.ID);
        ret.add(Defend_Bard.ID);
        //*/
        // 1 riposte
        ret.add(Riposte.ID);
        // 1 music
        ret.add(Inspire.ID);

        return ret;
    }

    @Override
    public ArrayList<String> getStartingRelics()
    {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(PitchPipe.ID);
        UnlockTracker.markRelicAsSeen(PitchPipe.ID);
        ret.add(Lute.ID);
        UnlockTracker.markRelicAsSeen(Lute.ID);
        return ret;
    }

    @Override
    public CharSelectInfo getLoadout()
    {
        return new CharSelectInfo("Bard", "M U S I C", START_HP, START_HP, START_ORBS, 99, 5,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass)
    {
        return "the Bard";
    }

    @Override
    public AbstractCard.CardColor getCardColor()
    {
        return Enums.COLOR;
    }

    @Override
    public Color getCardRenderColor()
    {
        // TODO
        return new Color(Color.ROYAL);
    }

    @Override
    public Color getCardTrailColor()
    {
        // TODO
        return new Color(Color.ROYAL);
    }

    @Override
    public AbstractCard getStartCardForEvent()
    {
        return new Inspire();
    }

    @Override
    public int getAscensionMaxHPLoss()
    {
        return 4;
    }

    @Override
    public BitmapFont getEnergyNumFont()
    {
        return FontHelper.energyNumFontGreen;
    }

    @Override
    public void doCharSelectScreenSelectEffect()
    {
        // TODO
        CardCrawlGame.sound.playA("ATTACK_DAGGER_2", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey()
    {
        // TODO
        return "ATTACK_DAGGER_2";
    }

    @Override
    public String getLocalizedCharacterName()
    {
        return "A Bard";
    }

    @Override
    public AbstractPlayer newInstance()
    {
        return new Bard(name);
    }

    @Override
    public String getSpireHeartText()
    {
        // TODO
        return com.megacrit.cardcrawl.events.beyond.SpireHeart.DESCRIPTIONS[9];
    }

    @Override
    public Color getSlashAttackColor()
    {
        // TODO
        return Color.ROYAL;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect()
    {
        // TODO
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        };
    }

    @Override
    public String getVampireText()
    {
        // TODO
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[1];
    }
}
