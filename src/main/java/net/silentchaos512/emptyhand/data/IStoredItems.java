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

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public interface IStoredItems extends INBTSerializable<CompoundTag> {

    public static final int SIZE = 2;
    public static final int TUTORIAL_STAGES = 2;

    //// METHODS ////

    int getTutorialStage();

    void setTutorialStage(final int stage);

    Container getInventory();

    void setInventory(final Container inventory);

    int getLockedSlot();

    void setLockedSlot(final int lockedSlot);

    //// HELPER METHODS ////

    default void setItem(final int slot, final ItemStack itemStack) {
        getInventory().setItem(slot, itemStack);
        getInventory().setChanged();
    }

    default ItemStack getItem(final int slot) {
        return getInventory().getItem(slot);
    }

    default void setItem(final InteractionHand hand, final ItemStack itemStack) {
        getInventory().setItem(hand.ordinal(), itemStack);
        getInventory().setChanged();
    }

    default ItemStack getItem(final InteractionHand hand) {
        return getInventory().getItem(hand.ordinal());
    }

    default void setItem(final EquipmentSlot slot, final ItemStack itemStack) {
        if(slot.getType() != EquipmentSlot.Type.HAND) {
            return;
        }
        getInventory().setItem(slot.getIndex(), itemStack);
        getInventory().setChanged();
    }

    default ItemStack getItem(final EquipmentSlot slot) {
        if(slot.getType() != EquipmentSlot.Type.HAND) {
            return ItemStack.EMPTY;
        }
        return getInventory().getItem(slot.getIndex());
    }

    default void clearLockedSlot() {
        setLockedSlot(-1);
    }

    default NonNullList<ItemStack> toList() {
        final NonNullList<ItemStack> items = NonNullList.withSize(getInventory().getContainerSize(), ItemStack.EMPTY);
        for(int i = 0, size = getInventory().getContainerSize(); i < size; i++) {
            items.set(i, getItem(i));
        }
        return items;
    }

    //// NBT ////

    static final String KEY_INVENTORY = "Items";
    static final String KEY_SLOT = "Slot";
    static final String KEY_LOCKED_SLOT = "LockedSlot";
    static final String KEY_TUTORIAL_STAGE = "Tutorial";

    @Override
    default CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listNBT = new ListTag();
        // write inventory slots to NBT
        for (int i = 0; i < getInventory().getContainerSize(); i++) {
            ItemStack stack = getInventory().getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag slotNBT = new CompoundTag();
                slotNBT.putByte(KEY_SLOT, (byte) i);
                stack.save(slotNBT);
                listNBT.add(slotNBT);
            }
        }
        tag.put(KEY_INVENTORY, listNBT);
        tag.putInt(KEY_TUTORIAL_STAGE, getTutorialStage());
        tag.putInt(KEY_LOCKED_SLOT, getLockedSlot());
        return tag;
    }

    @Override
    default void deserializeNBT(CompoundTag tag) {
        final ListTag list = tag.getList(KEY_INVENTORY, Tag.TAG_COMPOUND);
        // read inventory slots from NBT
        for (int i = 0; i < list.size(); i++) {
            CompoundTag slotNBT = list.getCompound(i);
            int slotNum = slotNBT.getByte(KEY_SLOT) & 0xFF;
            if (slotNum < getInventory().getContainerSize()) {
                getInventory().setItem(slotNum, ItemStack.of(slotNBT));
            }
        }
        setTutorialStage(tag.getInt(KEY_TUTORIAL_STAGE));
        setLockedSlot(tag.getInt(KEY_LOCKED_SLOT));
    }
}
