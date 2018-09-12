/*
 * EmptyHand -- Config
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

package net.silentchaos512.emptyhand.config;

import net.silentchaos512.emptyhand.EmptyHand;
import net.silentchaos512.lib.client.gui.HudAnchor;
import net.silentchaos512.lib.config.ConfigBaseNew;
import net.silentchaos512.lib.config.ConfigOption;
import net.silentchaos512.lib.util.I18nHelper;
import net.silentchaos512.lib.util.LogHelper;

public class Config extends ConfigBaseNew {
    public static final String CAT_HUD = "hud";

    @ConfigOption(name = "OffsetX", category = CAT_HUD)
    @ConfigOption.Comment("HUD position offset")
    public static int hudOffsetX = 124;
    @ConfigOption(name = "OffsetY", category = CAT_HUD)
    @ConfigOption.Comment("HUD position offset")
    public static int hudOffsetY = -19;

    public static HudAnchor hudAnchor = HudAnchor.BOTTOM_CENTER;

    public static final Config INSTANCE = new Config(EmptyHand.MOD_ID);

    private Config(String modId) {
        super(modId);
    }

    @Override
    public void load() {
        try {
            super.load();

            hudAnchor = loadEnum("Anchor", CAT_HUD, HudAnchor.class, HudAnchor.BOTTOM_CENTER,
                    "Anchor position for HUD elements");
        } catch (Exception ex) {
            EmptyHand.log.fatal("Failed to load config file!");
            EmptyHand.log.catching(ex);
        }
    }

    @Override
    public I18nHelper i18n() {
        return EmptyHand.i18n;
    }

    @Override
    public LogHelper log() {
        return EmptyHand.log;
    }
}
