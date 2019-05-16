package com.evacipated.cardcrawl.mod.bard.events;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.relics.MagicTuningFork;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class SpireTroupe extends AbstractImageEvent
{
    public static final String ID = BardMod.makeID("SpireTroupe");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String INTRO_MSG = DESCRIPTIONS[0];
    private static final String LEAVE_MSG = DESCRIPTIONS[1];
    private static final String PLAY_1_MSG = DESCRIPTIONS[2];
    private static final String COMPLETE_MSG = DESCRIPTIONS[3];
    private static final String BARD_OFFER_MSG = DESCRIPTIONS[4];
    private static final String BARD_REFUSE_MSG = DESCRIPTIONS[5];
    private static final String BARD_JOIN_1_MSG = DESCRIPTIONS[6];
    private static final String BARD_JOIN_2_MSG = DESCRIPTIONS[7];

    private static final String OPT_TICKET = OPTIONS[0];
    private static final String OPT_TICKET_LOCKED = OPTIONS[1];
    private static final String OPT_LEAVE = OPTIONS[2];
    private static final String OPT_CONTINUE = OPTIONS[3];
    private static final String OPT_BARD_JOIN = OPTIONS[4];
    private static final String OPT_BARD_REFUSE = OPTIONS[5];
    private static final String OPT_BARD_SLEEP = OPTIONS[6];
    private static final String OPT_BARD_END = OPTIONS[7];

    private static final int MIN_GOLD_COST = 50;
    private static final int MAX_GOLD_COST = 75;
    private static final int HEAL_AMT = 20;

    private CurrentScreen curScreen = CurrentScreen.INTRO;
    private int goldCost;

    private enum CurrentScreen
    {
        INTRO, PLAY_1, BARD_OFFER, BARD_JOIN_1, BARD_JOIN_2, COMPLETE
    }

    public SpireTroupe()
    {
        super(NAME, INTRO_MSG, null);

        goldCost = getGoldCost();
        if (goldCost != 0) {
            imageEventText.setDialogOption(String.format(OPT_TICKET, goldCost, HEAL_AMT));
        } else {
            imageEventText.setDialogOption(String.format(OPT_TICKET_LOCKED, MIN_GOLD_COST), true);
        }
        imageEventText.setDialogOption(OPT_LEAVE);
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (curScreen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0: // Ticket
                        AbstractDungeon.player.loseGold(goldCost);

                        curScreen = CurrentScreen.PLAY_1;
                        imageEventText.updateBodyText(PLAY_1_MSG);

                        imageEventText.updateDialogOption(0, OPT_CONTINUE);
                        imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Leave
                        curScreen = CurrentScreen.COMPLETE;
                        imageEventText.updateBodyText(LEAVE_MSG);

                        imageEventText.updateDialogOption(0, OPT_LEAVE);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case PLAY_1:
                AbstractDungeon.player.heal(HEAL_AMT, true);

                imageEventText.clearAllDialogs();
                if (AbstractDungeon.player.chosenClass == Bard.Enums.BARD) {
                    curScreen = CurrentScreen.BARD_OFFER;
                    StringBuilder name = new StringBuilder();
                    for (String word : CardCrawlGame.playerName.split(" ")) {
                        name.append("#y~").append(word).append("~ ");
                    }
                    imageEventText.updateBodyText(COMPLETE_MSG + String.format(BARD_OFFER_MSG, name.toString()));
                    imageEventText.setDialogOption(OPT_BARD_JOIN);
                    imageEventText.setDialogOption(OPT_BARD_REFUSE);
                } else {
                    curScreen = CurrentScreen.COMPLETE;
                    imageEventText.updateBodyText(COMPLETE_MSG);
                    imageEventText.setDialogOption(OPT_LEAVE);
                }
                break;
            case BARD_OFFER:
                switch (buttonPressed) {
                    case 0: // Join
                        curScreen = CurrentScreen.BARD_JOIN_1;
                        String name = CardCrawlGame.playerName;
                        String b_name = FontHelper.colorString(name, "b").substring(2);
                        String p_name = FontHelper.colorString(name, "p").substring(2);
                        String y_name = FontHelper.colorString(name, "y").substring(2);
                        imageEventText.updateBodyText(String.format(BARD_JOIN_1_MSG, name, name, b_name, p_name, y_name, name));

                        imageEventText.updateDialogOption(0, OPT_BARD_SLEEP, CardLibrary.getCopy(Pain.ID));
                        imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Refuse
                        curScreen = CurrentScreen.COMPLETE;
                        imageEventText.updateBodyText(BARD_REFUSE_MSG);

                        imageEventText.updateDialogOption(0, OPT_LEAVE);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case BARD_JOIN_1:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2f, Settings.HEIGHT / 2f, new MagicTuningFork());
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Pain(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));

                curScreen = CurrentScreen.COMPLETE;
                imageEventText.updateBodyText(BARD_JOIN_2_MSG);

                imageEventText.updateDialogOption(0, OPT_BARD_END);
                imageEventText.clearRemainingOptions();
                break;
            case BARD_JOIN_2:
                curScreen = CurrentScreen.COMPLETE;
                imageEventText.updateBodyText("");

                imageEventText.updateDialogOption(0, OPT_LEAVE);
                imageEventText.clearRemainingOptions();
                break;
            case COMPLETE:
                openMap();
                break;
        }
    }

    private int getGoldCost()
    {
        if (AbstractDungeon.player.gold < MIN_GOLD_COST) {
            return 0;
        }
        if (AbstractDungeon.player.gold > MAX_GOLD_COST) {
            return AbstractDungeon.miscRng.random(MIN_GOLD_COST, MAX_GOLD_COST);
        }
        return AbstractDungeon.miscRng.random(MIN_GOLD_COST, AbstractDungeon.player.gold);
    }
}
