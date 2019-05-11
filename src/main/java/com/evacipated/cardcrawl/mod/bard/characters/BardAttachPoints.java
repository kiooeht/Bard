package com.evacipated.cardcrawl.mod.bard.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.*;

import java.util.HashMap;
import java.util.Map;

public class BardAttachPoints
{
    private static Map<String, AttachPoint> map;

    static
    {
        map = new HashMap<>();
        // Head
        map.put(SmilingMask.ID,
                new AttachPoint(
                        SmilingMask.ID,
                        "head_side",
                        "images/relics/merchantMask.png",
                        0.6f, 0.6f,
                        0, 0,
                        30
                )
        );
        map.put(BustedCrown.ID,
                new AttachPoint(
                        BustedCrown.ID,
                        "head_top",
                        "images/relics/crown.png",
                        0.4f, 0.4f,
                        0, 5,
                        0
                )
        );
        // Waist
        map.put(Lantern.ID,
                new AttachPoint(
                        Lantern.ID,
                        "waist",
                        "images/relics/lantern.png",
                        0.7f, 0.7f,
                        -10, -10,
                        -20
                )
        );
        // Ground
        map.put(Toolbox.ID,
                new AttachPoint(
                        Toolbox.ID,
                        "ground3",
                        "images/relics/toolbox.png",
                        -0.9f, 0.9f,
                        110, 30,
                        0
                )
        );
        map.put(BirdFacedUrn.ID,
                new AttachPoint(
                        BirdFacedUrn.ID,
                        "ground2",
                        "images/largeRelics/bird_urn.png",
                        -0.7f, 0.7f,
                        -93, 55,
                        0
                )
        );
        map.put(AncientTeaSet.ID,
                new AttachPoint(
                        AncientTeaSet.ID,
                        "ground1",
                        "images/largeRelics/tea_set.png",
                       0.7f, 0.7f,
                        150, 20,
                        0
                )
        );
        // Bag pipes
        map.put(Anchor.ID,
                new AttachPoint(
                        Anchor.ID,
                        "bag_pipes_handle",
                        "images/relics/anchor.png",
                        1, 1,
                        30, -5,
                        120
                )
        );
    }

    public static void attachRelic(Skeleton skeleton, String relicID)
    {
        AttachPoint attachPoint = map.get(relicID);
        if (attachPoint == null) {
            return;
        }

        String attachName = attachPoint.attachName + "_attach";
        int slotIndex = skeleton.findSlotIndex(attachName);
        if (slotIndex < 0) {
            return;
        }

        Texture tex = BardMod.assets.loadImage(attachPoint.imgPath);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(tex);
        RegionAttachment attachment = new RegionAttachment(attachPoint.ID);
        attachment.setRegion(region);
        attachment.setWidth(tex.getWidth());
        attachment.setHeight(tex.getHeight());
        attachment.setX(attachPoint.x * Settings.scale);
        attachment.setY(attachPoint.y * Settings.scale);
        attachment.setScaleX(attachPoint.scaleX * Settings.scale);
        attachment.setScaleY(attachPoint.scaleY * Settings.scale);
        attachment.setRotation(attachPoint.angle);
        attachment.updateOffset();

        Skin skin = skeleton.getData().getDefaultSkin();
        skin.addAttachment(slotIndex, attachment.getName(), attachment);

        skeleton.setAttachment(attachName, attachment.getName());
    }

    public static class AttachPoint
    {
        final String ID;
        final String attachName;
        final String imgPath;
        final float scaleX;
        final float scaleY;
        final float x;
        final float y;
        final float angle;

        public AttachPoint(
                String id, String attachName, String img,
                float scaleX, float scaleY,
                float x, float y,
                float angle
        )
        {
            ID = id;
            this.attachName = attachName;
            imgPath = img;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.x = x;
            this.y = y;
            this.angle = angle;
        }
    }
}
