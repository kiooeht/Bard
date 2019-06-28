package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.mod.bard.relics.BagPipes;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        clz=SingleCardViewPopup.class,
        method="renderTips"
)
public class BagPipesSCVNotesPreviewPatch
{
    private static StringBuilder builder = new StringBuilder();

    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"card", "t"}
    )
    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card, ArrayList<PowerTip> t)
    {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(BagPipes.ID) || !BardMod.bagPipeNotesTooltip()) {
            return;
        }

        List<AbstractNote> notes = AbstractBardCard.determineNoteTypes(card);
        if (notes != null && !notes.isEmpty()) {
            builder.setLength(0);
            for (AbstractNote note : notes) {
                builder.append(note.cardCode()).append(" ");
            }
            t.add(new PowerTip(BagPipes.NAME, builder.toString()));
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "keywords");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
