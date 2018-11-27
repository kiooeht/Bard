package com.evacipated.cardcrawl.mod.bard.powers.interfaces;

import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;

public interface OnNoteQueuedPower
{
    AbstractNote onNoteQueued(AbstractNote note);
}
