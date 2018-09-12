package net.silentchaos512.emptyhand.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.silentchaos512.emptyhand.EmptyHand;
import net.silentchaos512.emptyhand.EmptyHandData;
import net.silentchaos512.emptyhand.network.MessageEmptyHand;
import net.silentchaos512.lib.client.key.KeyTrackerSL;
import org.lwjgl.input.Keyboard;

public class KeyTracker extends KeyTrackerSL {
    public static KeyTracker instance = new KeyTracker();

    private KeyBinding keyEmptyHand;

    private KeyTracker() {
        super(EmptyHand.MOD_NAME);
        keyEmptyHand = createBinding("Empty Hand", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_GRAVE);
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        if (keyEmptyHand.isPressed()) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            EmptyHandData.processEmptyHandRequest(player);
            EmptyHand.network.wrapper.sendToServer(new MessageEmptyHand(player));
        }
    }

    public String getKeybindDisplayName() {
        return this.keyEmptyHand.getDisplayName();
    }
}
