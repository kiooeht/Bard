package com.evacipated.cardcrawl.mod.bard;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.cards.variables.InspirationVariable;
import com.evacipated.cardcrawl.mod.bard.cards.variables.MagicNumber2;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.helpers.MelodyManager;
import com.evacipated.cardcrawl.mod.bard.melodies.*;
import com.evacipated.cardcrawl.mod.bard.notes.AttackNote;
import com.evacipated.cardcrawl.mod.bard.notes.BlockNote;
import com.evacipated.cardcrawl.mod.bard.notes.BuffNote;
import com.evacipated.cardcrawl.mod.bard.notes.DebuffNote;
import com.evacipated.cardcrawl.mod.bard.relics.AbstractBardRelic;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.*;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

@SpireInitializer
public class BardMod implements
        PostInitializeSubscriber,
        EditCharactersSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber
{
    public static final Logger logger = LogManager.getLogger(BardMod.class.getSimpleName());

    private static final Color COLOR = CardHelper.getColor(65, 105, 225);

    public static TextureAtlas noteAtlas;
    public static TextureAtlas powerAtlas;

    public static void initialize()
    {
        BaseMod.subscribe(new BardMod());

        System.out.println(Color.ROYAL.toString());

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

        MelodyManager.addMelody(new AttackUpSmallMelody());
        MelodyManager.addMelody(new AttackUpLargeMelody());
        MelodyManager.addMelody(new DefenseUpSmallMelody());
        MelodyManager.addMelody(new DefenseUpLargeMelody());
        MelodyManager.addMelody(new DivineProtectionMelody());
    }

    public static String makeID(String id)
    {
        return "bard:" + id;
    }

    public static String assetPath(String path)
    {
        return "bardAssets/" + path;
    }

    @Override
    public void receivePostInitialize()
    {
        noteAtlas = new TextureAtlas(Gdx.files.internal(assetPath("images/notes/notes.atlas")));
        powerAtlas = new TextureAtlas(Gdx.files.internal(assetPath("images/powers/powers.atlas")));
    }

    @Override
    public void receiveEditCharacters()
    {
        BaseMod.addCharacter(
                new Bard(CardCrawlGame.playerName),
                "images/ui/charSelect/silentButton.png",
                "images/ui/charSelect/silentPortrait.jpg",
                Bard.Enums.BARD
        );
    }

    @Override
    public void receiveEditCards()
    {
        BaseMod.addDynamicVariable(new MagicNumber2());
        BaseMod.addDynamicVariable(new InspirationVariable());

        try {
            autoAddCards();
        } catch (URISyntaxException | IllegalAccessException | InstantiationException | NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveEditRelics()
    {
        try {
            autoAddRelics();
        } catch (URISyntaxException | IllegalAccessException | InstantiationException | NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveEditStrings()
    {
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath("localization/CardStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath("localization/RelicStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath("localization/PowerStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, assetPath("localization/UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(assetPath("localization/Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    private static void autoAddCards()
            throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, CannotCompileException
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
            AbstractCard card = (AbstractCard) Loader.getClassPool().toClass(cls).newInstance();
            BaseMod.addCard(card);
            if (cls.hasAnnotation(CardNoSeen.class)) {
                UnlockTracker.hardUnlockOverride(card.cardID);
            } else {
                UnlockTracker.unlockCard(card.cardID);
            }
        }
    }

    private static void autoAddRelics()
            throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, CannotCompileException
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
            AbstractBardRelic relic = (AbstractBardRelic) Loader.getClassPool().toClass(cls).newInstance();
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
}
