/*
 * EmptyHand -- MessageSyncItems
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
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.silentchaos512.emptyhand.EmptyHand;
import net.silentchaos512.emptyhand.EmptyHandData;
import net.silentchaos512.lib.event.ClientTicks;
import net.silentchaos512.lib.network.MessageSL;

public class MessageSyncItems extends MessageSL {
    @SuppressWarnings("WeakerAccess")
    public ItemStack mainHand;
    @SuppressWarnings("WeakerAccess")
    public ItemStack offHand;

    @SuppressWarnings("unused")
    public MessageSyncItems() {
    }

    public MessageSyncItems(ItemStack mainHand, ItemStack offHand) {
        this.mainHand = mainHand;
        this.offHand = offHand;
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        ClientTicks.scheduleAction(() -> {
            EntityPlayer player = EmptyHand.proxy.getClientPlayer();
            if (player != null)
                EmptyHandData.setStoredStacks(player, this.mainHand, this.offHand);
        });
        return null;
    }
}
