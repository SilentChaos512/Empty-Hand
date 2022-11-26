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

package net.silentchaos512.emptyhand.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyTracker {

    public static final String KEY_EMPTY_HAND = "key.emptyHand";

    public static KeyMapping keyEmptyHand;

    public static void register() {
        keyEmptyHand = new KeyMapping(KEY_EMPTY_HAND, KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.grave.accent"), "key.categories.inventory");
        ClientRegistry.registerKeyBinding(keyEmptyHand);
    }
}
