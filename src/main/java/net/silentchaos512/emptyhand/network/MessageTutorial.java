/*
 * EmptyHand -- MessageTutorial
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
import net.silentchaos512.emptyhand.EmptyHand;
import net.silentchaos512.emptyhand.client.KeyTracker;
import net.silentchaos512.lib.event.ClientTicks;
import net.silentchaos512.lib.network.MessageSL;
import net.silentchaos512.lib.util.ChatHelper;

public class MessageTutorial extends MessageSL {
    public int index;

    public MessageTutorial() {
        this(0);
    }

    public MessageTutorial(int index) {
        this.index = index;
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        ClientTicks.INSTANCE.scheduleAction(() -> {
            EntityPlayer player = EmptyHand.proxy.getClientPlayer();
            String keyName = KeyTracker.instance.getKeybindDisplayName();
            ChatHelper.sendMessage(player, EmptyHand.i18n.miscText("tutorial" + this.index, keyName));
        });
        return null;
    }
}
