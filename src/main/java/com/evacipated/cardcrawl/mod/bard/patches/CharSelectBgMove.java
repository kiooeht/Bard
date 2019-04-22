package com.evacipated.cardcrawl.mod.bard.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        clz=CharacterSelectScreen.class,
        method="render"
)
public class CharSelectBgMove
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            private int found = 0;

            @Override
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                    ++found;
                    if (found == 2) {
                        m.replace("{" +
                                "if (" + CardCrawlGame.class.getName() + ".chosenCharacter == " + Bard.Enums.class.getName() + ".BARD) {" +
                                "$3 += 100 * " + Settings.class.getName() + ".scale;" +
                                "}" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }
            }
        };
    }
}
