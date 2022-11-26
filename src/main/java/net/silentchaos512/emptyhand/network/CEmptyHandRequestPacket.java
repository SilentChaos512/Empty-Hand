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
import net.silentchaos512.emptyhand.EmptyHandData;

import java.util.function.Supplier;

/**
 * Sent from the client to the server to empty hands
 **/
public class CEmptyHandRequestPacket {
    
    public CEmptyHandRequestPacket() { }

    /**
     * Reads the raw packet data from the data stream.
     *
     * @param buf the PacketBuffer
     * @return a new instance of a CEmptyHandRequestPacket based on the PacketBuffer
     */
    public static CEmptyHandRequestPacket fromBytes(final FriendlyByteBuf buf) {
        return new CEmptyHandRequestPacket();
    }

    /**
     * Writes the raw packet data to the data stream.
     *
     * @param msg the CEmptyHandRequestPacket
     * @param buf the PacketBuffer
     */
    public static void toBytes(final CEmptyHandRequestPacket msg, final FriendlyByteBuf buf) {
        // do nothing
    }

    /**
     * Handles the packet when it is received.
     *
     * @param message         the CEmptyHandRequestPacket
     * @param contextSupplier the NetworkEvent.Context supplier
     */
    public static void handlePacket(final CEmptyHandRequestPacket message, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER && context.getSender() != null) {
            context.enqueueWork(() -> {
                EmptyHandData.processEmptyHandRequest(context.getSender());
            });
        }
        context.setPacketHandled(true);
    }
}
