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

package net.silentchaos512.emptyhand.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.silentchaos512.emptyhand.client.HudAnchor;

public class EmptyHandConfig {

    private final ForgeConfigSpec.IntValue HUD_OFFSET_X;
    private int hudOffsetX;

    private final ForgeConfigSpec.IntValue HUD_OFFSET_Y;
    private int hudOffsetY;

    private final ForgeConfigSpec.BooleanValue SHOW_TUTORIAL;
    private boolean showTutorial;

    private final ForgeConfigSpec.EnumValue<HudAnchor> HUD_ANCHOR;
    private HudAnchor hudAnchor;

    public EmptyHandConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("hud");
        HUD_ANCHOR = builder.comment("Anchor position for HUD elements").defineEnum("anchor", HudAnchor.BOTTOM_MIDDLE);
        HUD_OFFSET_X = builder.comment("HUD position X offset").defineInRange("offset_x", 124, -4000, 4000);
        HUD_OFFSET_Y = builder.comment("HUD position Y offset").defineInRange("offset_y", -19, -4000, 4000);
        builder.pop();
        builder.push("tutorial");
        SHOW_TUTORIAL = builder.comment("Show the tutorial messages in chat for new worlds until the tutorial is completed.").define("show_tutorial", true);

        builder.pop();
    }

    /**
     * Helper method to read config values to fields.
     * This allows us to skip several config lookups
     * in the background when we want to query a value.
     */
    public void bake() {
        // hud
        hudAnchor = HUD_ANCHOR.get();
        hudOffsetX = HUD_OFFSET_X.get();
        hudOffsetY = HUD_OFFSET_Y.get();
        // tutorial
        showTutorial = SHOW_TUTORIAL.get();
    }

    public int getHudOffsetX() {
        return hudOffsetX;
    }

    public int getHudOffsetY() {
        return hudOffsetY;
    }

    public boolean showTutorial() {
        return showTutorial;
    }

    public HudAnchor getHudAnchor() {
        return hudAnchor;
    }
}
