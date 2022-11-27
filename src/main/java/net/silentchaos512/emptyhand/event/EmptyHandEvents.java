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

package net.silentchaos512.emptyhand.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.silentchaos512.emptyhand.EmptyHand;
import net.silentchaos512.emptyhand.EmptyHandData;
import net.silentchaos512.emptyhand.data.IStoredItems;
import net.silentchaos512.emptyhand.data.StoredItemsCapability;

public final class EmptyHandEvents {

    @Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModHandler {

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            // register capability objects
            event.register(StoredItemsCapability.class);
        }

        @SubscribeEvent
        public static void onLoadConfig(final ModConfigEvent.Loading event) {
            // update config
            EmptyHand.CONFIG.bake();
        }

        @SubscribeEvent
        public static void onReloadConfig(final ModConfigEvent.Reloading event) {
            // update config
            EmptyHand.CONFIG.bake();
        }
    }

    @Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE)
    public static final class ForgeHandler {

        @SubscribeEvent
        public static void onAttachEntityCapabilities(final AttachCapabilitiesEvent<Entity> event) {
            // attach entity capabilities
            if(event.getObject() instanceof Player player) {
                event.addCapability(StoredItemsCapability.REGISTRY_NAME, new StoredItemsCapability.Provider(player));
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
            if(event.getPlayer() instanceof ServerPlayer) {
                // sync capability
                final ServerPlayer player = (ServerPlayer) event.getPlayer();
                EmptyHandData.syncDataWithClient(player);
                EmptyHandData.sendTutorialMessage(player);
            }
        }

        /**
         * Used to ensure that stored items persist across deaths
         * @param event the player clone event
         */
        @SubscribeEvent
        public static void onPlayerClone(final PlayerEvent.Clone event) {
            // revive capabilities in order to copy to the clone
            event.getOriginal().reviveCaps();
            LazyOptional<IStoredItems> original = event.getOriginal().getCapability(EmptyHand.STORED_ITEMS);
            LazyOptional<IStoredItems> copy = event.getPlayer().getCapability(EmptyHand.STORED_ITEMS);
            if(original.isPresent() && copy.isPresent() && event.isWasDeath()) {
                copy.ifPresent(f -> f.deserializeNBT(original.orElse(StoredItemsCapability.EMPTY).serializeNBT()));
            }
        }

        @SubscribeEvent(priority = EventPriority.LOW)
        public static void onPlayerDeath(final LivingDeathEvent event) {
            if(event.getEntityLiving() instanceof ServerPlayer) {
                final ServerPlayer player = (ServerPlayer) event.getEntityLiving();
                player.getCapability(EmptyHand.STORED_ITEMS).ifPresent(c -> {
                    // check gamerule
                    if(!player.getLevel().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                        // drop items
                        for (int i = 0, n = c.getInventory().getContainerSize(); i < n; ++i) {
                            ItemStack itemstack = c.getInventory().removeItemNoUpdate(i);
                            if (!itemstack.isEmpty()) {
                                player.drop(itemstack, true);
                            }
                        }
                    }
                });
                // sync capability
                EmptyHandData.syncDataWithClient(player);
            }
        }
    }
}
