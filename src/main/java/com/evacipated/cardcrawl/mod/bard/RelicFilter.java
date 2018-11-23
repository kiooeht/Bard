package com.evacipated.cardcrawl.mod.bard;

import org.clapper.util.classutil.ClassFilter;
import org.clapper.util.classutil.ClassFinder;
import org.clapper.util.classutil.ClassInfo;

public class RelicFilter implements ClassFilter
{
    private static final String PACKAGE = "com.evacipated.cardcrawl.mod.bard.relics.";

    @Override
    public boolean accept(ClassInfo classInfo, ClassFinder classFinder)
    {
        if (classInfo.getClassName().startsWith(PACKAGE)) {
            return true;
        }
        return false;
    }
}
