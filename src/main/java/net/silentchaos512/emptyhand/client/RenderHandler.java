package net.silentchaos512.emptyhand.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.emptyhand.EmptyHandData;
import net.silentchaos512.emptyhand.config.Config;

public class RenderHandler extends Gui {
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != ElementType.HOTBAR) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        ItemStack mainHand = EmptyHandData.getStoredStack(player, EnumHand.MAIN_HAND);
        ItemStack offHand = EmptyHandData.getStoredStack(player, EnumHand.OFF_HAND);
        if (mainHand.isEmpty() && offHand.isEmpty()) return;

        RenderItem itemRenderer = mc.getRenderItem();
        ScaledResolution res = new ScaledResolution(mc);

        int x = Config.hudAnchor.offsetX(res.getScaledWidth(), Config.hudOffsetX);
        int y = Config.hudAnchor.offsetY(res.getScaledHeight(), Config.hudOffsetY);

        GlStateManager.pushMatrix();
        itemRenderer.renderItemAndEffectIntoGUI(offHand, x - 8, y);
        itemRenderer.renderItemAndEffectIntoGUI(mainHand, x + 8, y);
        GlStateManager.popMatrix();
    }
}
