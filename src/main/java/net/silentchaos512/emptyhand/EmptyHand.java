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

import mcp.MethodsReturnNonnullByDefault;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.emptyhand.network.MessageEmptyHand;
import net.silentchaos512.emptyhand.network.MessageSyncItems;
import net.silentchaos512.emptyhand.network.MessageTutorial;
import net.silentchaos512.emptyhand.proxy.CommonProxy;
import net.silentchaos512.lib.base.IModBase;
import net.silentchaos512.lib.network.NetworkHandlerSL;
import net.silentchaos512.lib.registry.SRegistry;
import net.silentchaos512.lib.util.I18nHelper;
import net.silentchaos512.lib.util.LogHelper;

@Mod(modid = EmptyHand.MOD_ID,
        name = EmptyHand.MOD_NAME,
        version = EmptyHand.VERSION,
        dependencies = EmptyHand.DEPENDENCIES,
        guiFactory = "net.silentchaos512.emptyhand.client.GuiFactoryEH")
@MethodsReturnNonnullByDefault
@SuppressWarnings({"WeakerAccess", "unused"})
public class EmptyHand implements IModBase {
    public static final String MOD_ID = "emptyhand";
    public static final String MOD_NAME = "Empty Hand";
    public static final String VERSION = "0.1.2";
    public static final String VERSION_SILENTLIB = "3.0.6";
    public static final int BUILD_NUM = 0;
    public static final String DEPENDENCIES = "required-after:forge@[14.23.3.2655,);" +
            "required-after:silentlib@[" + VERSION_SILENTLIB + ",);";
    public static final String RESOURCE_PREFIX = MOD_ID + ":";

    public static LogHelper log = new LogHelper(MOD_NAME, BUILD_NUM);
    public static I18nHelper i18n = new I18nHelper(MOD_ID, log, true);
    public static SRegistry registry = new SRegistry();
    public static NetworkHandlerSL network;

    @Instance(MOD_ID)
    public static EmptyHand instance;

    @SidedProxy(clientSide = "net.silentchaos512.emptyhand.proxy.ClientProxy", serverSide = "net.silentchaos512.emptyhand.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        registry.setMod(this);
        network = new NetworkHandlerSL(MOD_ID);

        network.register(MessageEmptyHand.class, Side.SERVER);
        network.register(MessageSyncItems.class, Side.CLIENT);
        network.register(MessageTutorial.class, Side.CLIENT);

        proxy.preInit(EmptyHand.registry, event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(EmptyHand.registry, event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(EmptyHand.registry, event);
    }

    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Override
    public String getModName() {
        return MOD_NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public int getBuildNum() {
        return BUILD_NUM;
    }

    @Override
    public LogHelper getLog() {
        return log;
    }
}
