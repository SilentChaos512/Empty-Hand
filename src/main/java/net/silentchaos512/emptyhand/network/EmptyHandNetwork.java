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

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.silentchaos512.emptyhand.EmptyHand;

import java.util.Optional;

public final class EmptyHandNetwork {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(EmptyHand.MOD_ID, "channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        int messageId = 0;
        CHANNEL.registerMessage(messageId++, CEmptyHandRequestPacket.class, CEmptyHandRequestPacket::toBytes, CEmptyHandRequestPacket::fromBytes, CEmptyHandRequestPacket::handlePacket, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(messageId++, SUpdateTutorialPacket.class, SUpdateTutorialPacket::toBytes, SUpdateTutorialPacket::fromBytes, SUpdateTutorialPacket::handlePacket, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(messageId++, SSyncCapabilityPacket.class, SSyncCapabilityPacket::toBytes, SSyncCapabilityPacket::fromBytes, SSyncCapabilityPacket::handlePacket, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
