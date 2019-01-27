package com.evacipated.cardcrawl.mod.bard.relics.interfaces;

import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;

public interface OnNoteQueuedRelic
{
    AbstractNote onNoteQueued(AbstractNote note);
}
