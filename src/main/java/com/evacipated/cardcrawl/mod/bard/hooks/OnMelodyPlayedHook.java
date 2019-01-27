package com.evacipated.cardcrawl.mod.bard.hooks;

import com.evacipated.cardcrawl.mod.bard.melodies.AbstractMelody;

public interface OnMelodyPlayedHook
{
    void onMelodyPlayed(AbstractMelody melody);
}
