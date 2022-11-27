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

package net.silentchaos512.emptyhand.network;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.silentchaos512.emptyhand.client.EmptyHandClientEvents;
import net.silentchaos512.emptyhand.data.IStoredItems;

import java.util.function.Supplier;

/**
 * Sent from the server to the client to sync the capability
 **/
public class SSyncCapabilityPacket {

    protected int size;
    protected NonNullList<ItemStack> items;
    protected int lockedSlot;
    protected int tutorialStage;
    
    public SSyncCapabilityPacket(final IStoredItems capability) {
        this(capability.toList(), capability.getLockedSlot(), capability.getTutorialStage());
    }
    
    public SSyncCapabilityPacket(final NonNullList<ItemStack> items, final int lockedSlot, final int tutorialStage) {
        this.items = items;
        this.size = items.size();
        this.lockedSlot = lockedSlot;
        this.tutorialStage = tutorialStage;
    }
    

    /**
     * Reads the raw packet data from the data stream.
     *
     * @param buf the PacketBuffer
     * @return a new instance of a SSyncCapabilityPacket based on the PacketBuffer
     */
    public static SSyncCapabilityPacket fromBytes(final FriendlyByteBuf buf) {
        int size = buf.readInt();
        int lockedSlot = buf.readInt();
        int tutorialStage = buf.readInt();
        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        for(int i = 0; i < size; i++) {
            items.set(i, buf.readItem());
        }
        return new SSyncCapabilityPacket(items, lockedSlot, tutorialStage);
    }

    /**
     * Writes the raw packet data to the data stream.
     *
     * @param msg the SSyncCapabilityPacket
     * @param buf the PacketBuffer
     */
    public static void toBytes(final SSyncCapabilityPacket msg, final FriendlyByteBuf buf) {
        buf.writeInt(msg.size);
        buf.writeInt(msg.lockedSlot);
        buf.writeInt(msg.tutorialStage);
        for(ItemStack item : msg.items) {
            buf.writeItem(item);
        }
    }

    /**
     * Handles the packet when it is received.
     *
     * @param message         the SSyncCapabilityPacket
     * @param contextSupplier the NetworkEvent.Context supplier
     */
    public static void handlePacket(final SSyncCapabilityPacket message, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                EmptyHandClientEvents.syncCapability(message.items, message.lockedSlot, message.tutorialStage);
            });
        }
        context.setPacketHandled(true);
    }
}
