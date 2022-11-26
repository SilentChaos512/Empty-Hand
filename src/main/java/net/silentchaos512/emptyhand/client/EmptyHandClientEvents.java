/*
 * EmptyHand -- EmptyHand
 * Copyright (C) 2022 Skyler James
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.emptyhand.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.emptyhand.EmptyHand;
import net.silentchaos512.emptyhand.EmptyHandData;
import net.silentchaos512.emptyhand.data.IStoredItems;
import net.silentchaos512.emptyhand.data.StoredItemsCapability;
import net.silentchaos512.emptyhand.network.CEmptyHandRequestPacket;
import net.silentchaos512.emptyhand.network.EmptyHandNetwork;

public final class EmptyHandClientEvents {

    @Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModHandler {

        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            event.enqueueWork(KeyTracker::register);
            // register overlay
            OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Empty Hand", (gui, poseStack, partialTick, width, height) -> {
                final Minecraft mc = Minecraft.getInstance();
                final Player player = mc.player;
                // determine the items to render
                final IStoredItems storedItems = player.getCapability(EmptyHand.STORED_ITEMS).orElse(StoredItemsCapability.EMPTY);
                final ItemStack mainhand = storedItems.getItem(InteractionHand.MAIN_HAND);
                final ItemStack offhand = storedItems.getItem(InteractionHand.OFF_HAND);
                // do not render when there are no items
                if(mainhand.isEmpty() && offhand.isEmpty()) {
                    return;
                }
                // determine render position
                final Tuple<Integer, Integer> offset = EmptyHand.CONFIG.getHudAnchor().getWithOffset(width, height, EmptyHand.CONFIG.getHudOffsetX(), EmptyHand.CONFIG.getHudOffsetY());
                final int renderX = offset.getA();
                final int renderY = offset.getB();
                // render the items
                mc.getItemRenderer().renderAndDecorateItem(mainhand, renderX - 8, renderY);
                mc.getItemRenderer().renderAndDecorateItem(offhand, renderX + 8, renderY);
            });
        }

    }

    @Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static final class ForgeHandler {

        @SubscribeEvent
        public static void onKeyInput(final InputEvent.KeyInputEvent event) {
            if (KeyTracker.keyEmptyHand.consumeClick()) {
                Player player = Minecraft.getInstance().player;
                EmptyHandData.processEmptyHandRequest(player);
                EmptyHandNetwork.CHANNEL.sendToServer(new CEmptyHandRequestPacket());
            }
        }
    }

    public static void sendTutorialMessage(int index) {
        if(!EmptyHand.CONFIG.showTutorial()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if(null == player) {
            return;
        }
        // construct message
        final Component message = new TranslatableComponent("misc.emptyhand.tutorial.prefix").withStyle(ChatFormatting.YELLOW);
        message.getSiblings().add(new TextComponent(" "));
        message.getSiblings().add(new TranslatableComponent("misc.emptyhand.tutorial" + index, KeyTracker.keyEmptyHand.getKey().getDisplayName()).withStyle(ChatFormatting.WHITE));
        // send message to player
        player.displayClientMessage(message, false);
    }

    public static void syncItems(final NonNullList<ItemStack> items) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        player.getCapability(EmptyHand.STORED_ITEMS).ifPresent(c -> {
            for(int i = 0, n = Math.min(items.size(), c.getInventory().getContainerSize()); i < n; i++) {
                c.setItem(i, items.get(i));
            }
        });
    }

    public static void syncCapability(final NonNullList<ItemStack> items, final int lockedSlot, final int tutorialStage) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        player.getCapability(EmptyHand.STORED_ITEMS).ifPresent(c -> {
            // update items
            for(int i = 0, n = Math.min(items.size(), c.getInventory().getContainerSize()); i < n; i++) {
                c.setItem(i, items.get(i));
            }
            // update locked slot and tutorial stage
            c.setLockedSlot(lockedSlot);
            c.setTutorialStage(tutorialStage);
        });
    }
}
