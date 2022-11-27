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

package net.silentchaos512.emptyhand.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.emptyhand.EmptyHand;

public class StoredItemsCapability implements IStoredItems {

    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(EmptyHand.MOD_ID, "stored_items");

    public static final IStoredItems EMPTY = new StoredItemsCapability();

    private Container inventory = new SimpleContainer(SIZE);
    private int tutorialStage;
    private int lockedSlot;

    @Override
    public int getTutorialStage() {
        return tutorialStage;
    }

    @Override
    public void setTutorialStage(int stage) {
        this.tutorialStage = stage;
    }

    @Override
    public Container getInventory() {
        return inventory;
    }

    @Override
    public void setInventory(Container inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getLockedSlot() {
        return lockedSlot;
    }

    @Override
    public void setLockedSlot(int lockedSlot) {
        this.lockedSlot = lockedSlot;
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        private final IStoredItems instance;
        private final LazyOptional<IStoredItems> storage;

        public Provider(Player player) {
            instance = new StoredItemsCapability();
            storage = LazyOptional.of(() -> instance);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if(cap == EmptyHand.STORED_ITEMS) {
                return storage.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }
}
