package com.evacipated.cardcrawl.mod.bard.cards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.notes.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

    public AbstractBardCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, "FAKE TITLE", null, cost, "FAKE DESCRIPTION", type, color, rarity, target);

        loadCardImage(getImage(id, type));

        cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        name = NAME = cardStrings.NAME;
        rawDescription = DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
        initializeTitle();
        initializeDescription();
    }

    protected static String getImage(String id, CardType type)
    {
        id = id.replaceFirst("^" + BardMod.makeID(""), "");
        char c[] = id.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        id = new String(c);
        return BardMod.assetPath(String.format("images/cards/%s/%s.png", type.name().toLowerCase(), id));
    }

    @Override
    public void loadCardImage(String img)
    {
        Texture cardTexture = BardMod.assets.loadImage(img);
        if (cardTexture != null) {
            textureImg = img;
            cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            int tw = cardTexture.getWidth();
            int th = cardTexture.getHeight();
            TextureAtlas.AtlasRegion cardImg = new TextureAtlas.AtlasRegion(cardTexture, 0, 0, tw, th);
            ReflectionHacks.setPrivateInherited(this, CustomCard.class, "portrait", cardImg);
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
                                otherCardBuffDebuff.put(card.cardID, buffType[0]);
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
