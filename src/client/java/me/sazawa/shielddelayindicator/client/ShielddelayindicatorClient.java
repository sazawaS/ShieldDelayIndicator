package me.sazawa.shielddelayindicator.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import javax.swing.*;
import java.text.Format;

public class ShielddelayindicatorClient implements ClientModInitializer {

    static public boolean isRightClickPressed = false;

    static int shiledDelayTicks = -1;
    static boolean lastState = false;


    public void onInitializeClient() {

        HudRenderCallback.EVENT.register((DrawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (!ShieldDelayConfig.enabled) return;
            if (shiledDelayTicks != -1) {

                String text = "✓ " + shiledDelayTicks;
                Formatting color = Formatting.RED;

                if (shiledDelayTicks == 3 || shiledDelayTicks == 2 || shiledDelayTicks == 1) {
                    color = Formatting.YELLOW;
                }

                if (shiledDelayTicks == 0) {
                    color = Formatting.GREEN;
                }

                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                int x = screenWidth / 2;
                int y = screenHeight / 2;

                DrawContext.drawText(
                        client.textRenderer,
                        Text.literal(text).formatted(color),
                        x - client.textRenderer.getWidth(text)/2 + ShieldDelayConfig.offsetX,
                        y + ShieldDelayConfig.offsetY,
                        0xFFFFFF,
                        true
                );
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = client.player;
            MinecraftClient mc = MinecraftClient.getInstance();

            if (player != null) {

                boolean isBlocking = false;
                if (player.getMainHandStack().getItem() == Items.SHIELD || player.getOffHandStack().getItem() == Items.SHIELD) {
                    isBlocking = isRightClickPressed;
                }

                if (isBlocking){

                    //Just started to shield
                    if (lastState == false) {
                        shiledDelayTicks = 5;
                    } else {
                        if (shiledDelayTicks > 0) {
                            shiledDelayTicks -= 1;
                        }
                    }

                } else {
                    shiledDelayTicks = -1;
                }

                lastState = isBlocking;
            }
        });
    }

}
