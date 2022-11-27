/*
 * EmptyHand -- EmptyHand
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

package net.silentchaos512.emptyhand;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.silentchaos512.emptyhand.config.EmptyHandConfig;
import net.silentchaos512.emptyhand.data.IStoredItems;
import net.silentchaos512.emptyhand.network.EmptyHandNetwork;

@Mod(EmptyHand.MOD_ID)
public class EmptyHand {

    public static final String MOD_ID = "emptyhand";

    // create config
    private static final ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
    public static EmptyHandConfig CONFIG = new EmptyHandConfig(CONFIG_BUILDER);
    private static final ForgeConfigSpec CONFIG_SPEC = CONFIG_BUILDER.build();

    // create capability
    public static Capability<IStoredItems> STORED_ITEMS = CapabilityManager.get(new CapabilityToken<>(){});


    public EmptyHand() {
        // register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG_SPEC);
        // register network and packets
        EmptyHandNetwork.register();

    }



}
