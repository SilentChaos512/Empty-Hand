/*
 * EmptyHand -- EmptyHandData
 * Copyright (C) 2018 SilentChaos512
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

package net.silentchaos512.emptyhand;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.silentchaos512.emptyhand.data.IStoredItems;
import net.silentchaos512.emptyhand.data.StoredItemsCapability;
import net.silentchaos512.emptyhand.network.EmptyHandNetwork;
import net.silentchaos512.emptyhand.network.SSyncCapabilityPacket;
import net.silentchaos512.emptyhand.network.SUpdateTutorialPacket;

public final class EmptyHandData {

    private EmptyHandData() {
    }


    /**
     * Gets the stored item for the given hand, or {@link ItemStack#EMPTY} if nothing is stored.
     */
    public static ItemStack getStoredStack(Player player, InteractionHand hand) {
        final IStoredItems storedItems = player.getCapability(EmptyHand.STORED_ITEMS).orElse(StoredItemsCapability.EMPTY);
        return storedItems.getItem(hand);
    }

    public static void processEmptyHandRequest(Player player) {
        swapForHand(player, InteractionHand.MAIN_HAND);
        swapForHand(player, InteractionHand.OFF_HAND);

        setLockedSlot(player);

        if (player instanceof ServerPlayer) {
            ServerPlayer playerMP = (ServerPlayer) player;
            syncDataWithClient(playerMP);
            incrementTutorialStage(playerMP);
            sendTutorialMessage(playerMP);
        }
    }

    private static void setLockedSlot(Player player) {
        player.getCapability(EmptyHand.STORED_ITEMS).ifPresent(c -> {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                int slot = player.getInventory().selected;
                c.setLockedSlot(slot);
            } else {
                c.clearLockedSlot();
            }
        });
    }

    /**
     * Swaps the item in the player's given hand with the item in Empty Hand's "storage slot".
     */
    private static void swapForHand(Player player, InteractionHand hand) {
        player.getCapability(EmptyHand.STORED_ITEMS).ifPresent(c -> {
            ItemStack current = player.getItemInHand(hand);
            ItemStack previous = c.getItem(hand);
            player.setItemInHand(hand, previous);
            c.setItem(hand, current);
        });
    }

    /**
     * Sends a tutorial message to the player, if appropriate.
     */
    public static void sendTutorialMessage(ServerPlayer player) {
        player.getCapability(EmptyHand.STORED_ITEMS).ifPresent(c -> {
            int completedStage = c.getTutorialStage();
            if (completedStage >= IStoredItems.TUTORIAL_STAGES) {
                return;
            }
            int currentStage = completedStage + 1;
            EmptyHandNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SUpdateTutorialPacket(currentStage));
        });
    }

    /**
     * Increment the tutorial stage, which determines which message will be shown, if any.
     */
    private static void incrementTutorialStage(ServerPlayer player) {
        player.getCapability(EmptyHand.STORED_ITEMS).ifPresent(c -> {
            int completedStage = c.getTutorialStage();
            if (completedStage < IStoredItems.TUTORIAL_STAGES) {
                c.setTutorialStage(completedStage + 1);
            }
            // send capability to client player
            EmptyHandNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SSyncCapabilityPacket(c));
        });
    }

    public static void syncDataWithClient(ServerPlayer player) {
        player.getCapability(EmptyHand.STORED_ITEMS).ifPresent(c -> {
            // send capability to client player
            EmptyHandNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SSyncCapabilityPacket(c));
        });
    }
}
