package com.evacipated.cardcrawl.mod.bard;

import basemod.*;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.cards.variables.InspirationVariable;
import com.evacipated.cardcrawl.mod.bard.cards.variables.MagicNumber2;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.characters.NoteQueue;
import com.evacipated.cardcrawl.mod.bard.events.SpireTroupe;
import com.evacipated.cardcrawl.mod.bard.helpers.AssetLoader;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.melodies.*;
import com.evacipated.cardcrawl.mod.bard.notes.*;
import com.evacipated.cardcrawl.mod.bard.patches.PlayerNoteQueuePatches;
import com.evacipated.cardcrawl.mod.bard.potions.BottledSong;
import com.evacipated.cardcrawl.mod.bard.potions.InspiredBrew;
import com.evacipated.cardcrawl.mod.bard.relics.AbstractBardRelic;
import com.evacipated.cardcrawl.mod.bard.ui.MelodiesPanel;
import com.evacipated.cardcrawl.mod.bard.ui.NotesPanel;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class BardMod implements
        PostInitializeSubscriber,
        EditCharactersSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        PostPlayerUpdateSubscriber,
        RelicGetSubscriber,
        PostUpdateSubscriber
{
    public static final Logger logger = LogManager.getLogger(BardMod.class.getSimpleName());

    public static final String ID;
    public static final String NAME;

    public static final Color COLOR = CardHelper.getColor(65, 105, 225);

    public static ShaderProgram colorTintShader;

    public static AssetLoader assets = new AssetLoader();
    public static TextureAtlas noteAtlas;

    public static NotesPanel notesPanel;
    public static MelodiesPanel melodiesPanel;

    public static Prefs prefs;
    private static ModLabeledToggleButton bagpipesTooltipBtn;
    private static ModLabeledToggleButton bagpipesUIBtn;
    private static ModLabeledToggleButton bagpipesBothBtn;

    // Crossover checks
    public static final boolean hasHubris;

    static
    {
        String tmpID = "bard";
        String tmpNAME = "Bard";
        try {
            Properties properties = new Properties();
            properties.load(BardMod.class.getResourceAsStream("/META-INF/bard_version.prop"));
            tmpID = properties.getProperty("id");
            tmpNAME = properties.getProperty("name");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ID = tmpID;
        NAME = tmpNAME;

        hasHubris = Loader.isModLoaded("hubris");
        if (hasHubris) {
            logger.info("Detected Hubris");
        }
    }

    public static void initialize()
    {
        BaseMod.subscribe(new BardMod());

        BaseMod.addColor(Bard.Enums.COLOR,
                COLOR,
                assetPath("images/cardui/512/bg_attack_royal.png"), assetPath("images/cardui/512/bg_skill_royal.png"),
                assetPath("images/cardui/512/bg_power_royal.png"), assetPath("images/cardui/512/card_royal_orb.png"),
                assetPath("images/cardui/1024/bg_attack_royal.png"), assetPath("images/cardui/1024/bg_skill_royal.png"),
                        assetPath("images/cardui/1024/bg_power_royal.png"), assetPath("images/cardui/1024/card_royal_orb.png"),
                assetPath("images/cardui/orb.png"));

        MelodyManager.addNote(AttackNote.get());
        MelodyManager.addNote(BlockNote.get());
        MelodyManager.addNote(BuffNote.get());
        MelodyManager.addNote(DebuffNote.get());
        MelodyManager.addNote(RestNote.get());
        MelodyManager.addNote(WildCardNote.get());
    }

    public static void sideload()
    {
        BardMod bardMod = new BardMod();
        BaseMod.subscribe(bardMod, PostInitializeSubscriber.class);
        BaseMod.subscribe(bardMod, EditStringsSubscriber.class);
        BaseMod.subscribe(bardMod, EditKeywordsSubscriber.class);
        BaseMod.subscribe(bardMod, PostPlayerUpdateSubscriber.class);
        BaseMod.subscribe(bardMod, PostUpdateSubscriber.class);

        BaseMod.addColor(Bard.Enums.COLOR,
                COLOR,
                assetPath("images/cardui/512/bg_attack_royal.png"), assetPath("images/cardui/512/bg_skill_royal.png"),
                assetPath("images/cardui/512/bg_power_royal.png"), assetPath("images/cardui/512/card_royal_orb.png"),
                assetPath("images/cardui/1024/bg_attack_royal.png"), assetPath("images/cardui/1024/bg_skill_royal.png"),
                assetPath("images/cardui/1024/bg_power_royal.png"), assetPath("images/cardui/1024/card_royal_orb.png"),
                assetPath("images/cardui/orb.png"));

        MelodyManager.addNote(AttackNote.get());
        MelodyManager.addNote(BlockNote.get());
        MelodyManager.addNote(BuffNote.get());
        MelodyManager.addNote(DebuffNote.get());
        MelodyManager.addNote(RestNote.get());
        MelodyManager.addNote(WildCardNote.get());
    }

    public static String makeID(String id)
    {
        return ID + ":" + id;
    }

    public static String assetPath(String path)
    {
        return "bardAssets/" + path;
    }

    private static int getBagPipeNotesType()
    {
        // Defaults to just the Card UI
        return prefs.getInteger("bagpipes_ui", 2);
    }

    public static boolean bagPipeNotesTooltip()
    {
        if (prefs == null) {
            return false;
        }
        return getBagPipeNotesType() == 3 || bagPipeNotesBoth();
    }

    public static boolean bagPipeNotesCardUI()
    {
        if (prefs == null) {
            return true;
        }
        return getBagPipeNotesType() == 2 || bagPipeNotesBoth();
    }

    public static boolean bagPipeNotesBoth()
    {
        if (prefs == null) {
            return true;
        }
        return getBagPipeNotesType() == 1;
    }

    public static NoteQueue getNoteQueue(AbstractPlayer player)
    {
        return PlayerNoteQueuePatches.NoteQueueField.noteQueue.get(player);
    }

    @Override
    public void receivePostInitialize()
    {
        prefs = SaveHelper.getPrefs("BardPrefs");

        colorTintShader = new ShaderProgram(
                Gdx.files.internal(BardMod.assetPath("shaders/colorTint/vertexShader.vs")),
                Gdx.files.internal(BardMod.assetPath("shaders/colorTint/fragShader.fs"))
        );

        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabel(
                "Bag Pipes",
                380, 720,
                Settings.GOLD_COLOR, FontHelper.charTitleFont,
                settingsPanel,
                update -> {}
        ));
        bagpipesTooltipBtn = new ModLabeledToggleButton("Tooltip",
                390, 610, Settings.CREAM_COLOR, FontHelper.charDescFont,
                getBagPipeNotesType() == 3, settingsPanel, l -> {},
                button ->
                {
                    if (prefs != null && button.enabled) {
                        bagpipesUIBtn.toggle.enabled = false;
                        bagpipesBothBtn.toggle.enabled = false;
                        prefs.putInteger("bagpipes_ui", 3);
                        prefs.flush();
                    } else if (!button.enabled) {
                        button.enabled = true;
                    }
                });
        bagpipesUIBtn = new ModLabeledToggleButton("Card UI",
                390, 640, Settings.CREAM_COLOR, FontHelper.charDescFont,
                getBagPipeNotesType() == 2, settingsPanel, l -> {},
                button ->
                {
                    if (prefs != null && button.enabled) {
                        bagpipesTooltipBtn.toggle.enabled = false;
                        bagpipesBothBtn.toggle.enabled = false;
                        prefs.putInteger("bagpipes_ui", 2);
                        prefs.flush();
                    } else if (!button.enabled) {
                        button.enabled = true;
                    }
                });
        bagpipesBothBtn = new ModLabeledToggleButton("Both",
                390, 670, Settings.CREAM_COLOR, FontHelper.charDescFont,
                getBagPipeNotesType() == 1, settingsPanel, l -> {},
                button ->
                {
                    if (prefs != null && button.enabled) {
                        bagpipesTooltipBtn.toggle.enabled = false;
                        bagpipesUIBtn.toggle.enabled = false;
                        prefs.putInteger("bagpipes_ui", 1);
                        prefs.flush();
                    } else if (!button.enabled) {
                        button.enabled = true;
                    }
                });
        settingsPanel.addUIElement(bagpipesTooltipBtn);
        settingsPanel.addUIElement(bagpipesUIBtn);
        settingsPanel.addUIElement(bagpipesBothBtn);

        BaseMod.registerModBadge(ImageMaster.loadImage(assetPath("images/modBadge.png")), NAME, "kiooeht", "TODO", settingsPanel);

        BaseMod.addEvent(SpireTroupe.ID, SpireTroupe.class, TheCity.ID);

        noteAtlas = assets.loadAtlas(assetPath("images/notes/notes.atlas"));

        MelodyManager.addMelody(new DamageSmallMelody());
        MelodyManager.addMelody(new DamageMediumMelody());
        MelodyManager.addMelody(new DamageLargeMelody());
        MelodyManager.addMelody(new BlockSmallMelody());
        MelodyManager.addMelody(new BlockMediumMelody());
        MelodyManager.addMelody(new BlockLargeMelody());
        MelodyManager.addMelody(new AttackUpSmallMelody());
        MelodyManager.addMelody(new DefenseUpSmallMelody());
        MelodyManager.addMelody(new InspireSmallMelody());
        MelodyManager.addMelody(new InspireLargeMelody());
        MelodyManager.addMelody(new WeakenSmallMelody());
        MelodyManager.addMelody(new VulnerabilitySmallMelody());
        MelodyManager.addMelody(new DrawMelody());
        MelodyManager.addMelody(new ArtifactMelody());
        MelodyManager.addMelody(new DivineProtectionMelody());

        BaseMod.addPotion(InspiredBrew.class, Color.ROYAL.cpy(), Color.ROYAL.cpy(), Color.ROYAL.cpy(), InspiredBrew.POTION_ID, Bard.Enums.BARD);
        BaseMod.addPotion(BottledSong.class, Color.GOLDENROD.cpy(), Color.GREEN.cpy(), null, BottledSong.POTION_ID, Bard.Enums.BARD);

        notesPanel = new NotesPanel();
        melodiesPanel = new MelodiesPanel();
    }

    @Override
    public void receiveEditCharacters()
    {
        BaseMod.addCharacter(
                new Bard(CardCrawlGame.playerName),
                assetPath("images/ui/charSelect/bardButton.png"),
                assetPath("images/ui/charSelect/bardPortrait.jpg"),
                Bard.Enums.BARD
        );
    }

    @Override
    public void receiveEditCards()
    {
        BaseMod.addDynamicVariable(new MagicNumber2());
        BaseMod.addDynamicVariable(new InspirationVariable());

        TextureAtlas cardAtlas = (TextureAtlas) ReflectionHacks.getPrivateStatic(AbstractCard.class, "cardAtlas");

        TextureAtlas myCardAtlas = assets.loadAtlas(assetPath("images/cards/cards.atlas"));
        for (TextureAtlas.AtlasRegion region : myCardAtlas.getRegions()) {
            cardAtlas.addRegion(ID + "/" + region.name, region);
        }

        new AutoAdd(ID)
                .packageFilter(AbstractBardCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditRelics()
    {
        new AutoAdd(ID)
                .packageFilter(AbstractBardRelic.class)
                .any(AbstractBardRelic.class, (info, relic) -> {
                    if (relic.color == null) {
                        BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, relic.color);
                    }
                    if (!info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    private static String makeLocPath(Settings.GameLanguage language, String filename)
    {
        String ret = "localization/";
        switch (language) {
            case ZHS:
                ret += "zhs/";
                break;
            default:
                ret += "eng/";
                break;
        }
        return assetPath(ret + filename + ".json");
    }

    private void loadLocFiles(Settings.GameLanguage language)
    {
        MelodyManager.loadMelodyStrings(makeLocPath(language, "MelodyStrings"));
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocPath(language, "CardStrings"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocPath(language, "RelicStrings"));
        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(language, "PotionStrings"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocPath(language, "PowerStrings"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocPath(language, "UIStrings"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocPath(language, "CharacterStrings"));
        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(language, "EventStrings"));
    }

    @Override
    public void receiveEditStrings()
    {
        loadLocFiles(Settings.GameLanguage.ENG);
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadLocFiles(Settings.language);
        }
    }

    private void loadLocKeywords(Settings.GameLanguage language)
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(makeLocPath(language, "Keywords")).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(ID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditKeywords()
    {
        loadLocKeywords(Settings.GameLanguage.ENG);
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadLocKeywords(Settings.language);
        }
    }

    @Override
    public void receiveAddAudio()
    {
        BaseMod.addAudio(makeID("ATTACK_HORN_1"), assetPath("audio/sound/SFX_Horn1.ogg"));
    }

    @Override
    public void receivePostPlayerUpdate()
    {
        notesPanel.update(AbstractDungeon.player);
        melodiesPanel.update(AbstractDungeon.player);
    }

    @Override
    public void receiveRelicGet(AbstractRelic relic)
    {
        if (AbstractDungeon.player instanceof Bard) {
            ((Bard) AbstractDungeon.player).attachRelic(relic);
        }
    }

    public static void renderNoteQueue(SpriteBatch sb, AbstractPlayer player)
    {
        notesPanel.render(sb, player);
    }

    public static void renderMelodiesPanel(SpriteBatch sb, AbstractPlayer player)
    {
        melodiesPanel.render(sb, player);
    }

    @Override
    public void receivePostUpdate()
    {
        WildCardNote.get().update();
    }
}
