package com.evacipated.cardcrawl.mod.bard.relics;

import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.mod.bard.notes.WildCardNote;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SelfPlayingFlute extends AbstractBardRelic
{
    public static final String ID = BardMod.makeID("SelfPlayingFlute");

    private static final String starterReplaceID = PitchPipe.ID;

    public SelfPlayingFlute()
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, Bard.Enums.COLOR);
    }

    @Override
    public String getUpdatedDescription()
    {
        // Colorize the starter relic's name
        String name = new PitchPipe().name;
        StringBuilder sb = new StringBuilder();
        for (String word : name.split(" ")) {
            sb.append("[#").append(BardMod.COLOR.toString()).append("]").append(word).append("[] ");
        }
        sb.setLength(sb.length()-1);
        sb.append("[#").append(BardMod.COLOR.toString()).append("]");

        return DESCRIPTIONS[0] + sb.toString() + DESCRIPTIONS[1];
    }

    @Override
    public void obtain() {
        // Replace the starter relic, or just give the relic if starter isn't found
        if (AbstractDungeon.player.hasRelic(starterReplaceID)) {
            for (int i=0; i<AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(starterReplaceID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public void atPreBattle()
    {
        addToBottom(new QueueNoteAction(WildCardNote.get()));
        addToBottom(new QueueNoteAction(WildCardNote.get()));
    }

    @Override
    public boolean canSpawn()
    {
        // Only spawn if player has the starter relic
        return AbstractDungeon.player.hasRelic(starterReplaceID);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new SelfPlayingFlute();
    }
}
