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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.silentchaos512.emptyhand.client.EmptyHandClientEvents;

import java.util.function.Supplier;

/**
 * Sent from the server to the client to display a tutorial message
 **/
public class SUpdateTutorialPacket {
    
    private int index;
    
    public SUpdateTutorialPacket(final int index) {
        this.index = index;
    }

    /**
     * Reads the raw packet data from the data stream.
     *
     * @param buf the PacketBuffer
     * @return a new instance of a SUpdateTutorialPacket based on the PacketBuffer
     */
    public static SUpdateTutorialPacket fromBytes(final FriendlyByteBuf buf) {
        int index = buf.readInt();
        return new SUpdateTutorialPacket(index);
    }

    /**
     * Writes the raw packet data to the data stream.
     *
     * @param msg the SUpdateTutorialPacket
     * @param buf the PacketBuffer
     */
    public static void toBytes(final SUpdateTutorialPacket msg, final FriendlyByteBuf buf) {
        buf.writeInt(msg.index);
    }

    /**
     * Handles the packet when it is received.
     *
     * @param message         the SUpdateTutorialPacket
     * @param contextSupplier the NetworkEvent.Context supplier
     */
    public static void handlePacket(final SUpdateTutorialPacket message, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                EmptyHandClientEvents.sendTutorialMessage(message.index);
            });
        }
        context.setPacketHandled(true);
    }
}
