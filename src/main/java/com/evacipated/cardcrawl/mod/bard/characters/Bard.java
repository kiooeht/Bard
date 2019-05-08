package com.evacipated.cardcrawl.mod.bard.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.cards.Defend_Bard;
import com.evacipated.cardcrawl.mod.bard.cards.Inspire;
import com.evacipated.cardcrawl.mod.bard.cards.Riposte;
import com.evacipated.cardcrawl.mod.bard.cards.Strike_Bard;
import com.evacipated.cardcrawl.mod.bard.relics.BagPipes;
import com.evacipated.cardcrawl.mod.bard.relics.PitchPipe;
import com.evacipated.cardcrawl.mod.bard.ui.EnergyOrbBard;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;

public class Bard extends CustomPlayer
{
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(BardMod.makeID("Bard"));
    private static final int START_HP = 70;
    private static final int ENERGY_PER_TURN = 3;
    private static final int START_ORBS = 0;
    public static final int MAX_NOTES = 4;

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
        super(name, Enums.BARD,
                new EnergyOrbBard(),
                new SpineAnimation(
                        BardMod.assetPath("images/characters/bard/idle/skeleton.atlas"),
                        BardMod.assetPath("images/characters/bard/idle/skeleton.json"),
                        1.6F
                )
        );

        dialogX = drawX + 0 * Settings.scale;
        dialogY = drawY + 170 * Settings.scale;

        initializeClass(
                null,
                BardMod.assetPath("images/characters/bard/shoulder2.png"),
                BardMod.assetPath("images/characters/bard/shoulder.png"),
                "images/characters/theSilent/corpse.png",
                getLoadout(), 0.0F, 0.0F, 240.0F, 280.0F, new EnergyManager(ENERGY_PER_TURN));

        AnimationState.TrackEntry e = state.setAnimation(0, "Idle", true);
        stateData.setMix("Hit", "Idle", 0.1f);
        e.setTimeScale(1f);

        BardMod.getNoteQueue(this).setMasterMaxNotes(MAX_NOTES);
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
        ret.add(BagPipes.ID);
        UnlockTracker.markRelicAsSeen(BagPipes.ID);
        return ret;
    }

    @Override
    public CharSelectInfo getLoadout()
    {
        return new CharSelectInfo(characterStrings.NAMES[0], characterStrings.TEXT[0], START_HP, START_HP, START_ORBS, 99, 5,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass)
    {
        return characterStrings.NAMES[1];
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
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect()
    {
        CardCrawlGame.sound.playA(BardMod.makeID("ATTACK_HORN_1"), MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey()
    {
        return BardMod.makeID("ATTACK_HORN_1");
    }

    @Override
    public String getLocalizedCharacterName()
    {
        return characterStrings.NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance()
    {
        return new Bard(name);
    }

    @Override
    public void damage(DamageInfo info)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > currentBlock) {
            AnimationState.TrackEntry e = state.setAnimation(0, "Hit", false);
            state.addAnimation(0,"Idle", true, 0.0f);
            e.setTimeScale(1f);
        }
        super.damage(info);
    }

    @Override
    public String getSpireHeartText()
    {
        return characterStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor()
    {
        return Color.ROYAL;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect()
    {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
        };
    }

    @Override
    public String getVampireText()
    {
        return characterStrings.TEXT[2];
    }
}
