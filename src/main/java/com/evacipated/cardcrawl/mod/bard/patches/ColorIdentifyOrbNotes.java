package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.bard.notes.AbstractNote;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

@SpirePatch(
        clz=FontHelper.class,
        method="renderSmartText",
        paramtypez={
                SpriteBatch.class,
                BitmapFont.class,
                String.class,
                float.class,
                float.class,
                float.class,
                float.class,
                Color.class
        }
)
@SpirePatch(
        clz=FontHelper.class,
        method="exampleNonWordWrappedText"
)
public class ColorIdentifyOrbNotes
{
    public static AbstractNote isNote = null;

    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(NewExpr e) throws CannotCompileException
            {
                if (e.getClassName().equals(Color.class.getName())) {
                    e.replace("$_ = " + ColorIdentifyOrbNotes.class.getName() + ".makeColor($$);");
                }
            }
        };
    }

    public static Color makeColor(float r, float g, float b, float a)
    {
        if (isNote != null) {
            Color c = new Color(isNote.color());
            c.a = a;
            isNote = null;
            return c;
        }
        return new Color(r, g, b, a);
    }
}
