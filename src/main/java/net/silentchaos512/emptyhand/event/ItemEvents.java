/*
 * EmptyHand -- ItemEvents
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

package net.silentchaos512.emptyhand.event;

import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.emptyhand.EmptyHand;
import net.silentchaos512.emptyhand.EmptyHandData;

@Mod.EventBusSubscriber(modid = EmptyHand.MOD_ID)
public final class ItemEvents {
    private ItemEvents() { }

    @SubscribeEvent
    public static void onEntityItemCollision(EntityItemPickupEvent event) {
        int lockedSlot = EmptyHandData.getLockedSlot(event.getEntityPlayer());
        if (lockedSlot >= 0) {
            // TODO Make sure item is not placed in locked slot
        }
    }
}
