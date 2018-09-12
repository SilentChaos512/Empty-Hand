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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.silentchaos512.emptyhand.network.MessageSyncItems;
import net.silentchaos512.emptyhand.network.MessageTutorial;
import net.silentchaos512.lib.util.PlayerHelper;

public class EmptyHandData {
    private static final String NBT_DATA = "EmptyHand_Data";
    private static final String NBT_STORED_STACK_PREFIX = "StoredStack";
    private static final String NBT_TUTORIAL_STAGE = "Tutorial";
    private static final int TUTORIAL_STAGES = 2;

    /**
     * Gets the stored item for the given hand, or {@link ItemStack#EMPTY} if nothing is stored.
     */
    public static ItemStack getStoredStack(EntityPlayer player, EnumHand hand) {
        NBTTagCompound tags = PlayerHelper.getPersistedDataSubcompound(player, NBT_DATA);
        final String key = getStoredStackKey(hand);

        if (tags.hasKey(key)) return new ItemStack(tags.getCompoundTag(key));
        else return ItemStack.EMPTY;
    }

    public static void processEmptyHandRequest(EntityPlayer player) {
        swapForHand(player, EnumHand.MAIN_HAND);
        swapForHand(player, EnumHand.OFF_HAND);

        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            syncDataWithClient(playerMP);
            incrementTutorialStage(playerMP);
            sendTutorialMessage(playerMP);
        }
    }

    /**
     * Sets the stored stacks. This is used exclusively by {@link MessageSyncItems} to sync items
     * from server to client, mostly on log-in.
     */
    public static void setStoredStacks(EntityPlayer player, ItemStack mainHand, ItemStack offHand) {
        if (player == null) return;
        NBTTagCompound tags = PlayerHelper.getPersistedDataSubcompound(player, NBT_DATA);
        tags.setTag(getStoredStackKey(EnumHand.MAIN_HAND), mainHand.writeToNBT(new NBTTagCompound()));
        tags.setTag(getStoredStackKey(EnumHand.OFF_HAND), offHand.writeToNBT(new NBTTagCompound()));
    }

    /**
     * Swaps the item in the player's given hand with the item in Empty Hand's "storage slot".
     */
    private static void swapForHand(EntityPlayer player, EnumHand hand) {
        final String key = getStoredStackKey(hand);
        NBTTagCompound tags = PlayerHelper.getPersistedDataSubcompound(player, NBT_DATA);
        ItemStack current = player.getHeldItem(hand);
        ItemStack previous = tags.hasKey(key) ? new ItemStack(tags.getCompoundTag(key)) : ItemStack.EMPTY;

        player.setHeldItem(hand, previous);
        NBTTagCompound itemCompound = current.writeToNBT(new NBTTagCompound());
        tags.setTag(key, itemCompound);
    }

    private static String getStoredStackKey(EnumHand hand) {
        return NBT_STORED_STACK_PREFIX + "_" + (hand == EnumHand.MAIN_HAND ? "Main" : "Off");
    }

    /**
     * Sends a tutorial message to the player, if appropriate.
     */
    private static void sendTutorialMessage(EntityPlayerMP player) {
        int completedStage = PlayerHelper.getPersistedDataSubcompound(player, NBT_DATA).getInteger(NBT_TUTORIAL_STAGE);
        if (completedStage >= TUTORIAL_STAGES) return;
        int currentStage = completedStage + 1;
        EmptyHand.network.wrapper.sendTo(new MessageTutorial(currentStage), player);
    }

    /**
     * Increment the tutorial stage, which determines which message will be shown, if any.
     */
    private static void incrementTutorialStage(EntityPlayerMP player) {
        NBTTagCompound tags = PlayerHelper.getPersistedDataSubcompound(player, NBT_DATA);
        int completedStage = tags.getInteger(NBT_TUTORIAL_STAGE);
        if (completedStage < TUTORIAL_STAGES)
            tags.setInteger(NBT_TUTORIAL_STAGE, completedStage + 1);
    }

    private static void syncDataWithClient(EntityPlayerMP player) {
        ItemStack mainHand = getStoredStack(player, EnumHand.MAIN_HAND);
        ItemStack offHand = getStoredStack(player, EnumHand.OFF_HAND);
        EmptyHand.network.wrapper.sendTo(new MessageSyncItems(mainHand, offHand), player);
    }

    @Mod.EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.player instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.player;
                syncDataWithClient(player);
                sendTutorialMessage(player);
            }
        }
    }
}
