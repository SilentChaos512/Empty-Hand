/*
 * EmptyHand -- MessageEmptyHand
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

package net.silentchaos512.emptyhand.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.silentchaos512.emptyhand.EmptyHandData;
import net.silentchaos512.lib.network.MessageSL;

public class MessageEmptyHand extends MessageSL {
    @SuppressWarnings("WeakerAccess")
    public String playerName;

    @SuppressWarnings("unused")
    public MessageEmptyHand() {
    }

    public MessageEmptyHand(EntityPlayer player) {
        this.playerName = player.getName();
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        EmptyHandData.processEmptyHandRequest(player);
        return null;
    }
}
