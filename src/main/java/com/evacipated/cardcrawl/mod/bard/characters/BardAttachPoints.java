package com.evacipated.cardcrawl.mod.bard.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.SlotData;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.evacipated.cardcrawl.mod.bard.relics.ConductorsBaton;
import com.evacipated.cardcrawl.mod.bard.relics.CoralOrchestra;
import com.evacipated.cardcrawl.mod.bard.relics.SelfPlayingFlute;
import com.evacipated.cardcrawl.mod.bard.relics.StrangeHarp;
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
                        "head_top_front",
                        "images/relics/crown.png",
                        0.4f, 0.4f,
                        0, 5,
                        0
                )
        );
        map.put(PeacePipe.ID,
                new AttachPoint(
                        PeacePipe.ID,
                        "head_mouth",
                        "images/relics/peacePipe.png",
                        -0.6f, 0.6f,
                        17, -10,
                        15
                )
        );
        map.put(GremlinHorn.ID,
                new AttachPoint(
                        GremlinHorn.ID,
                        "head_top_behind",
                        "images/relics/gremlinHorn.png",
                        0.8f, 0.8f,
                        0, 10,
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
        // Front leg
        map.put(StrangeHarp.ID,
                new AttachPoint(
                        StrangeHarp.ID,
                        "front_leg",
                        BardMod.assetPath("images/relics/strangeHarp.png"),
                        0.6f, 0.6f,
                        0, 0,
                        -20
                )
        );
        // Hand
        map.put(ConductorsBaton.ID,
                new AttachPoint(
                        ConductorsBaton.ID,
                        "hand",
                        BardMod.assetPath("images/relics/conductorsBaton.png"),
                        1, 1,
                        -15, 15,
                        -90
                )
        );
        // Body
        map.put(Ectoplasm.ID,
                new AttachPoint(
                        Ectoplasm.ID,
                        "pocket",
                        "images/relics/ectoplasm.png",
                        0.6f, 0.6f,
                        -2, 3,
                        62
                )
        );
        // Ground
        map.put(Toolbox.ID,
                new AttachPoint(
                        Toolbox.ID,
                        "ground_behind", 1,
                        "images/relics/toolbox.png",
                        -0.9f, 0.9f,
                        110, 30,
                        0
                )
        );
        map.put(BirdFacedUrn.ID,
                new AttachPoint(
                        BirdFacedUrn.ID,
                        "ground_behind", -1,
                        "images/largeRelics/bird_urn.png",
                        -0.7f, 0.7f,
                        -93, 55,
                        0
                )
        );
        map.put(AncientTeaSet.ID,
                new AttachPoint(
                        AncientTeaSet.ID,
                        "ground_behind", 2,
                        "images/largeRelics/tea_set.png",
                       0.6f, 0.6f,
                        157, 28,
                        0
                )
        );
        map.put(CoffeeDripper.ID,
                new AttachPoint(
                        CoffeeDripper.ID,
                        "ground_behind", 3,
                        "images/relics/coffeeDripper.png",
                        0.6f, 0.6f,
                        128, 23,
                        0
                )
        );
        map.put(Waffle.ID,
                new AttachPoint(
                        Waffle.ID,
                        "ground_behind", 4,
                        "images/relics/waffle.png",
                        -0.75f, 0.75f,
                        185, 5,
                        0
                )
        );
        map.put(IncenseBurner.ID,
                new AttachPoint(
                        IncenseBurner.ID,
                        "ground_infront", 1,
                        "images/relics/incenseBurner.png",
                        1, 1,
                        47, 22,
                        0
                )
        );
        map.put(TinyChest.ID,
                new AttachPoint(
                        TinyChest.ID,
                        "ground_behind", 5,
                        "images/relics/tinyChest.png",
                        -1, 1,
                        -135, 32,
                        3
                )
        );
        map.put(Torii.ID,
                new AttachPoint(
                        Torii.ID,
                        "ground_infront", -5,
                        "images/largeRelics/torii.png",
                        -0.9f, 0.9f,
                        255, 35,
                        0
                )
        );
        // Bag pipes
        map.put(Anchor.ID,
                new AttachPoint(
                        Anchor.ID,
                        "bag_pipes_handle_swing",
                        "images/relics/anchor.png",
                        1, 1,
                        30, -5,
                        120
                )
        );
        map.put(CoralOrchestra.ID,
                new AttachPoint(
                        CoralOrchestra.ID,
                        "bag_pipes_handle",
                        BardMod.assetPath("images/largeRelics/coralOrchestra.png"),
                        0.6f, 0.6f,
                        0, 0,
                        50
                )
        );
        map.put(HappyFlower.ID,
                new AttachPoint(
                        HappyFlower.ID,
                        "bag_pipes_head", 2,
                        "images/relics/sunflower.png",
                        1, 1,
                        0, 30,
                        0
                )
        );
        map.put(RegalPillow.ID,
                new AttachPoint(
                        RegalPillow.ID,
                        "bag_pipes_head", 1,
                        "images/relics/regal_pillow.png",
                        1, 1,
                        0, 2,
                        0
                )
        );
        map.put(Shuriken.ID,
                new AttachPoint(
                        Shuriken.ID,
                        "bag_pipes_head_behind", 1,
                        "images/relics/shuriken.png",
                        0.8f, 0.8f,
                        -64, -102,
                        25
                )
        );
        // Float
        map.put(SelfPlayingFlute.ID,
                new AttachPoint(
                        SelfPlayingFlute.ID,
                        "float1",
                        BardMod.assetPath("images/largeRelics/selfPlayingFlute.png"),
                        0.7f, 0.7f,
                        0, 0,
                        0
                )
        );
        map.put(MercuryHourglass.ID,
                new AttachPoint(
                        MercuryHourglass.ID,
                        "float_hourglass",
                        "images/relics/hourglass.png",
                        0.85f, 0.85f,
                        0, 0,
                        15
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

        if (attachPoint.attachIndex != null) {
            if (skeleton.findSlotIndex(attachName + attachPoint.attachIndex) < 0) {
                // Create a new slot for the attachment
                Slot origSlot = skeleton.findSlot(attachName);
                Slot slotClone = new Slot(new SlotData(origSlot.getData().getIndex(), attachName + attachPoint.attachIndex, origSlot.getBone().getData()), origSlot.getBone());
                slotClone.getData().setBlendMode(origSlot.getData().getBlendMode());
                skeleton.getSlots().insert(slotIndex, slotClone);

                // Add new slot to draw order
                Array<Slot> drawOrder = skeleton.getDrawOrder();
                int insertIndex = drawOrder.indexOf(origSlot, true);
                boolean found = false;
                for (int i = 0; i < drawOrder.size; ++i) {
                    Slot slot = drawOrder.get(i);
                    if (slot.getData().getName().startsWith(attachName)) {
                        found = true;
                        String tmp = slot.getData().getName().substring(attachName.length());
                        if (tmp.isEmpty()) {
                            tmp = "0";
                        }
                        int curIndex;
                        try {
                            curIndex = Integer.parseInt(tmp);
                        } catch (NumberFormatException ignore) {
                            continue;
                        }
                        insertIndex = i;
                        if (curIndex > attachPoint.attachIndex) {
                            break;
                        }
                    } else if (found) {
                        insertIndex = i;
                        break;
                    }
                }
                drawOrder.insert(insertIndex, slotClone);
                skeleton.setDrawOrder(drawOrder);
            }
            attachName = attachName + attachPoint.attachIndex;
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
        final Integer attachIndex;
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
            this(id, attachName, null, img, scaleX, scaleY, x, y, angle);
        }

        public AttachPoint(
                String id, String attachName, Integer attachIndex, String img,
                float scaleX, float scaleY,
                float x, float y,
                float angle
        )
        {
            ID = id;
            this.attachName = attachName;
            this.attachIndex = attachIndex;
            imgPath = img;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.x = x;
            this.y = y;
            this.angle = angle;
        }
    }
}
