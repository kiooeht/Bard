package com.evacipated.cardcrawl.mod.bard.hooks;

import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;

public interface OnNoteQueuedHook
{
    AbstractNote onNoteQueued(AbstractNote note);
}
