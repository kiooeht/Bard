package com.evacipated.cardcrawl.mod.bard;

import basemod.*;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.cards.variables.InspirationVariable;
import com.evacipated.cardcrawl.mod.bard.cards.variables.MagicNumber2;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.characters.NoteQueue;
import com.evacipated.cardcrawl.mod.bard.helpers.AssetLoader;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.melodies.*;
import com.evacipated.cardcrawl.mod.bard.notes.*;
import com.evacipated.cardcrawl.mod.bard.patches.PlayerNoteQueuePatches;
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
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
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
        PostPlayerUpdateSubscriber
{
    public static final Logger logger = LogManager.getLogger(BardMod.class.getSimpleName());

    public static final String ID;
    public static final String NAME;

    public static final Color COLOR = CardHelper.getColor(65, 105, 225);

    public static AssetLoader assets = new AssetLoader();
    public static TextureAtlas noteAtlas;

    public static NotesPanel notesPanel;
    public static MelodiesPanel melodiesPanel;

    public static Prefs prefs;
    private static ModLabeledToggleButton bagpipesTooltipBtn;
    private static ModLabeledToggleButton bagpipesUIBtn;
    private static ModLabeledToggleButton bagpipesBothBtn;

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
                null);

        MelodyManager.addNote(new AttackNote());
        MelodyManager.addNote(new BlockNote());
        MelodyManager.addNote(new BuffNote());
        MelodyManager.addNote(new DebuffNote());
        MelodyManager.addNote(new RestNote());
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

        noteAtlas = assets.loadAtlas(assetPath("images/notes/notes.atlas"));

        MelodyManager.addMelody(new AttackUpSmallMelody());
        //MelodyManager.addMelody(new AttackUpLargeMelody());
        MelodyManager.addMelody(new DefenseUpSmallMelody());
        //MelodyManager.addMelody(new DefenseUpLargeMelody());
        MelodyManager.addMelody(new DivineProtectionMelody());
        MelodyManager.addMelody(new DrawMelody());
        //MelodyManager.addMelody(new DrawUpMelody());
        //MelodyManager.addMelody(new EnergyMelody());
        MelodyManager.addMelody(new ArtifactMelody());
        MelodyManager.addMelody(new WeakenSmallMelody());
        MelodyManager.addMelody(new VulnerabilitySmallMelody());
        MelodyManager.addMelody(new InspireSmallMelody());
        MelodyManager.addMelody(new InspireLargeMelody());
        MelodyManager.addMelody(new DamageSmallMelody());
        MelodyManager.addMelody(new DamageLargeMelody());

        BaseMod.addPotion(InspiredBrew.class, Color.ROYAL.cpy(), Color.ROYAL.cpy(), Color.ROYAL.cpy(), InspiredBrew.POTION_ID, Bard.Enums.BARD);

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

        try {
            autoAddCards();
        } catch (URISyntaxException | IllegalAccessException | InstantiationException | NotFoundException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveEditRelics()
    {
        try {
            autoAddRelics();
        } catch (URISyntaxException | IllegalAccessException | InstantiationException | NotFoundException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveEditStrings()
    {
        MelodyManager.loadMelodyStrings(assetPath("localization/MelodyStrings.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath("localization/CardStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath("localization/RelicStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class, assetPath("localization/PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath("localization/PowerStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, assetPath("localization/UIStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, assetPath("localization/CharacterStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(assetPath("localization/Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(ID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveAddAudio()
    {
        BaseMod.addAudio(makeID("ATTACK_HORN_1"), assetPath("audio/sound/SFX_Horn1.ogg"));
    }

    private static void autoAddCards()
            throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, ClassNotFoundException
    {
        ClassFinder finder = new ClassFinder();
        URL url = BardMod.class.getProtectionDomain().getCodeSource().getLocation();
        finder.add(new File(url.toURI()));

        ClassFilter filter =
                new AndClassFilter(
                        new NotClassFilter(new InterfaceOnlyClassFilter()),
                        new NotClassFilter(new AbstractClassFilter()),
                        new ClassModifiersClassFilter(Modifier.PUBLIC),
                        new CardFilter()
                );
        Collection<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        for (ClassInfo classInfo : foundClasses) {
            CtClass cls = Loader.getClassPool().get(classInfo.getClassName());
            if (cls.hasAnnotation(CardIgnore.class)) {
                continue;
            }
            boolean isCard = false;
            CtClass superCls = cls;
            while (superCls != null) {
                superCls = superCls.getSuperclass();
                if (superCls == null) {
                    break;
                }
                if (superCls.getName().equals(AbstractCard.class.getName())) {
                    isCard = true;
                    break;
                }
            }
            if (!isCard) {
                continue;
            }
            System.out.println(classInfo.getClassName());
            AbstractCard card = (AbstractCard) Loader.getClassPool().getClassLoader().loadClass(cls.getName()).newInstance();
            BaseMod.addCard(card);
            if (cls.hasAnnotation(CardNoSeen.class)) {
                UnlockTracker.hardUnlockOverride(card.cardID);
            } else {
                UnlockTracker.unlockCard(card.cardID);
            }
        }
    }

    private static void autoAddRelics()
            throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, ClassNotFoundException
    {
        ClassFinder finder = new ClassFinder();
        URL url = BardMod.class.getProtectionDomain().getCodeSource().getLocation();
        finder.add(new File(url.toURI()));

        ClassFilter filter =
                new AndClassFilter(
                        new NotClassFilter(new InterfaceOnlyClassFilter()),
                        new NotClassFilter(new AbstractClassFilter()),
                        new ClassModifiersClassFilter(Modifier.PUBLIC),
                        new RelicFilter()
                );
        Collection<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        for (ClassInfo classInfo : foundClasses) {
            CtClass cls = Loader.getClassPool().get(classInfo.getClassName());
            if (cls.hasAnnotation(CardIgnore.class)) {
                continue;
            }
            boolean isRelic = false;
            CtClass superCls = cls;
            while (superCls != null) {
                superCls = superCls.getSuperclass();
                if (superCls == null) {
                    break;
                }
                if (superCls.getName().equals(AbstractBardRelic.class.getName())) {
                    isRelic = true;
                    break;
                }
            }
            if (!isRelic) {
                continue;
            }
            System.out.println(classInfo.getClassName());
            AbstractBardRelic relic = (AbstractBardRelic) Loader.getClassPool().getClassLoader().loadClass(cls.getName()).newInstance();
            if (relic.color == null) {
                BaseMod.addRelic(relic, RelicType.SHARED);
            } else {
                BaseMod.addRelicToCustomPool(relic, relic.color);
            }
            if (!cls.hasAnnotation(CardNoSeen.class)) {
                UnlockTracker.markRelicAsSeen(relic.relicId);
            }
        }
    }

    @Override
    public void receivePostPlayerUpdate()
    {
        notesPanel.update(AbstractDungeon.player);
        melodiesPanel.update(AbstractDungeon.player);
    }

    public static void renderNoteQueue(SpriteBatch sb, AbstractPlayer player)
    {
        notesPanel.render(sb, player);
        melodiesPanel.render(sb, player);
    }
}
