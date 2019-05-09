package com.evacipated.cardcrawl.mod.bard.cards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.notes.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.NewExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractBardCard extends CustomCard
{
    private static Map<String, Integer> otherCardBuffDebuff = new HashMap<>();

    protected final CardStrings cardStrings;
    protected final String NAME;
    protected final String DESCRIPTION;
    protected final String UPGRADE_DESCRIPTION;
    protected final String[] EXTENDED_DESCRIPTION;

    public int baseMagicNumber2 = -1;
    public int magicNumber2 = -1;
    public boolean isMagicNumber2Modified = false;
    public boolean upgradedMagicNumber2 = false;

    public int baseInspiration = -1;
    public int inspiration = -1;
    public boolean isInspirationModified = false;
    public boolean upgradedInspiration = false;

    protected Animation<TextureAtlas.AtlasRegion> animation;
    protected float animationTimer = 0;

    public AbstractBardCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, "FAKE TITLE", getRegionName(id, type), cost, "FAKE DESCRIPTION", type, color, rarity, target);

        cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        name = NAME = cardStrings.NAME;
        originalName = NAME;
        rawDescription = DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
        initializeTitle();
        initializeDescription();
    }

    protected static String getBaseImagePath(String id, CardType type)
    {
        id = id.replaceFirst("^" + BardMod.makeID(""), "");
        char c[] = id.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        id = new String(c);
        return String.format("%s/%s", type.name().toLowerCase(), id);
    }

    protected static CustomCard.RegionName getRegionName(String id, CardType type)
    {
        return new RegionName(String.format("%s/%s", BardMod.ID, getBaseImagePath(id, type)));
    }

    @Override
    public void loadCardImage(String img)
    {
        TextureAtlas cardAtlas = (TextureAtlas) ReflectionHacks.getPrivateStatic(AbstractCard.class, "cardAtlas");
        portrait = cardAtlas.findRegion(img);
    }

    @Override
    protected Texture getPortraitImage()
    {
        return ImageMaster.loadImage(BardMod.assetPath(String.format("images/1024Portraits/%s.png", getBaseImagePath(cardID, type))));
    }

    protected void loadAnimationByID(String id, float frameDuration)
    {
        TextureAtlas cardAtlas = (TextureAtlas) ReflectionHacks.getPrivateStatic(AbstractCard.class, "cardAtlas");

        Array<TextureAtlas.AtlasRegion> arr = new Array<>();
        int i = 0;
        while (true) {
            TextureAtlas.AtlasRegion tex = cardAtlas.findRegion(getRegionName(id + i, type).name);
            if (tex == null) {
                break;
            }
            arr.add(tex);
            ++i;
        }
        if (arr.size > 0) {
            animation = new Animation<>(frameDuration, arr, Animation.PlayMode.LOOP);
        }
    }

    protected void setCardImage(TextureAtlas.AtlasRegion cardTexture)
    {
        portrait = cardTexture;
    }

    protected TextureAtlas.AtlasRegion getCurrentFrame()
    {
        return animation.getKeyFrame(animationTimer);
    }

    @Override
    public void update()
    {
        super.update();

        if (animation != null) {
            animationTimer += Gdx.graphics.getDeltaTime();
            setCardImage(animation.getKeyFrame(animationTimer));
        }
    }

    protected void upgradeMagicNumber2(int amount)
    {
        baseMagicNumber2 += amount;
        magicNumber2 = baseMagicNumber2;
        upgradedMagicNumber2 = true;
    }

    protected void upgradeInspiration(int amount)
    {
        baseInspiration += amount;
        inspiration = baseInspiration;
        upgradedInspiration = true;
    }

    protected void addToTop(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToTop(action);
    }

    protected void addToBottom(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public abstract List<AbstractNote> getNotes();

    public static List<AbstractNote> determineNoteTypes(AbstractCard card)
    {
        if (card instanceof AbstractBardCard) {
            return ((AbstractBardCard) card).getNotes();
        }

        List<AbstractNote> notes = new ArrayList<>();
        if (isBlockGainingCard(card)) {
            notes.add(new BlockNote());
        }
        if (isDamageDealingCard(card)) {
            notes.add(new AttackNote());
        }
        if (isBuffCard(card)) {
            notes.add(new BuffNote());
        }
        if (isDebuffCard(card)) {
            notes.add(new DebuffNote());
        }
        return notes;
    }

    public static boolean isDamageDealingCard(AbstractCard card)
    {
        return card.baseDamage >= 0;
    }

    public static boolean isBlockGainingCard(AbstractCard card)
    {
        return card.baseBlock >= 0;
    }

    public static boolean isBuffCard(AbstractCard card)
    {
        if (!otherCardBuffDebuff.containsKey(card.cardID)) {
            calculateBuffDebuff(card);
        }

        Integer buffType = otherCardBuffDebuff.get(card.cardID);
        if (buffType != null) {
            if ((buffType & 1) == 1) {
                return true;
            }
        }

        return false;
    }

    public static boolean isDebuffCard(AbstractCard card)
    {
        if (!otherCardBuffDebuff.containsKey(card.cardID)) {
            calculateBuffDebuff(card);
        }

        Integer buffType = otherCardBuffDebuff.get(card.cardID);
        if (buffType != null) {
            if ((buffType & 2) == 2) {
                return true;
            }
        }

        return false;
    }

    private static void calculateBuffDebuff(AbstractCard card)
    {
        try {
            ClassPool pool = Loader.getClassPool();
            CtClass ctClass = pool.get(card.getClass().getName());
            ctClass.defrost();
            CtMethod useMethod = ctClass.getDeclaredMethod("use");

            final CardTarget[] targetType = {CardTarget.NONE};
            for (CtConstructor ctor : ctClass.getConstructors()) {
                if (ctor.callsSuper()) {
                    ctor.instrument(new ExprEditor()
                    {
                        @Override
                        public void edit(FieldAccess f)
                        {
                            if (f.getClassName().equals(CardTarget.class.getName())) {
                                targetType[0] = CardTarget.valueOf(f.getFieldName());
                            }
                        }
                    });
                }
            }

            useMethod.instrument(new ExprEditor() {
                @Override
                public void edit(NewExpr e)
                {
                    try {
                        CtConstructor ctConstructor = e.getConstructor();
                        CtClass cls = ctConstructor.getDeclaringClass();
                        if (cls != null) {
                            CtClass parent = cls;
                            do {
                                parent = parent.getSuperclass();
                            } while (parent != null && !parent.getName().equals(AbstractPower.class.getName()));
                            if (parent != null && parent.getName().equals(AbstractPower.class.getName())) {
                                // found a power
                                final int[] buffType = {0};
                                ExprEditor buffDebuffFinder = new ExprEditor() {
                                    @Override
                                    public void edit(FieldAccess f)
                                    {
                                        if (f.getClassName().equals(AbstractPower.PowerType.class.getName())) {
                                            switch (f.getFieldName()) {
                                                case "BUFF":
                                                    buffType[0] |= 1;
                                                    break;
                                                case "DEBUFF":
                                                    buffType[0] |= 2;
                                                    break;
                                                default:
                                                    System.out.println("idk what we found ??");
                                                    break;
                                            }
                                        }
                                    }
                                };
                                ctConstructor.instrument(buffDebuffFinder);
                                // Because StrengthPower sets its BUFF/DEBUFF status in updateDescription
                                // Others probably do too
                                CtMethod updateDescription = cls.getDeclaredMethod("updateDescription");
                                updateDescription.instrument(buffDebuffFinder);

                                if (buffType[0] == 0) {
                                    // powers default to being a buff
                                    buffType[0] = 1;
                                }
                                if ((buffType[0] & 1) == 1 && (buffType[0] & 2) == 2) {
                                    // Both buff and debuff, guess at which based on CardTarget
                                    switch (targetType[0]) {
                                        case SELF:
                                            buffType[0] = 1;
                                            break;
                                        case ENEMY:
                                        case ALL_ENEMY:
                                            buffType[0] = 2;
                                            break;
                                    }
                                }
                                otherCardBuffDebuff.compute(card.cardID, (k,v) -> (v == null ? 0 : v) | buffType[0]);
                            }
                        }
                    } catch (NotFoundException | CannotCompileException ignored) {
                    }
                }
            });
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
