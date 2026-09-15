package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.server.capability.AbilityData;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class AbilityClientEventHandler {
    /** Called once per rendered frame, after the frame has been rendered. */
    public static void onRenderTick(float partialTick) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
            for (Ability<?> ability : data.getAbilities()) {
                ability.onRenderTick(partialTick);
            }
        }
    }
}
