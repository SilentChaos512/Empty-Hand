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

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Tuple;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum HudAnchor implements StringRepresentable {
    TOP_LEFT("top_left", (w, h, x, y) -> new Tuple<>(x, y)),
    TOP_MIDDLE("top_middle", (w, h, x, y) -> new Tuple<>((w / 2) + x, y)),
    TOP_RIGHT("top_right", (w, h, x, y) -> new Tuple<>(w + x, y)),
    LEFT_MIDDLE("middle_left", (w, h, x, y) -> new Tuple<>(x, (h / 2) + y)),
    MIDDLE("middle", (w, h, x, y) -> new Tuple<>((w / 2) + x, (h / 2) + y)),
    RIGHT_MIDDLE("middle_right", (w, h, x, y) -> new Tuple<>(w + x, (h / 2) + y)),
    BOTTOM_LEFT("bottom_left", (w, h, x, y) -> new Tuple<>(x, h + y)),
    BOTTOM_MIDDLE("bottom_middle", (w, h, x, y) -> new Tuple<>((w / 2) + x, h + y)),
    BOTTOM_RIGHT("bottom_right", (w, h, x, y) -> new Tuple<>(w + x, h + y));

    private static final Map<String, HudAnchor> ID_MAP = ImmutableMap.copyOf(Arrays.stream(values())
            .collect(Collectors.<HudAnchor, String, HudAnchor>toMap(HudAnchor::getSerializedName, Function.identity())));

    private final String name;
    private final IWithOffset function;

    HudAnchor(final String name, final IWithOffset function) {
        this.name = name;
        this.function = function;
    }

    public static HudAnchor getByName(final String name) {
        return ID_MAP.getOrDefault(name, TOP_LEFT);
    }

    public Tuple<Integer, Integer> getWithOffset(final int screenWidth, final int screenHeight, final int offsetX, final int offsetY) {
        return function.apply(screenWidth, screenHeight, offsetX, offsetY);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @FunctionalInterface
    public static interface IWithOffset {
        public Tuple<Integer, Integer> apply(final int screenWidth, final int screenHeight, final int offsetX, final int offsetY);
    }
}
