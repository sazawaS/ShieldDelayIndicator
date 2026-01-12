package me.sazawa.shielddelayindicator.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethodStage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import javax.swing.*;
import java.text.Format;
import java.util.ResourceBundle;

public class ShielddelayindicatorClient implements ClientModInitializer {

    static public boolean isRightClickPressed = false;

    static public int shiledDelayTicks = -1;
    static public boolean lastState = false;


    private static final Identifier SHIELDDELAY_LAYER_IDENTIFIER = Identifier.of("shielddelayindicator", "shielddelayindicator");

    public void onInitializeClient() {

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


        HudElementRegistry.addFirst(
                Identifier.of("shielddelayindicator", "shield_delay"),
                this::render
        );

    }


    private void render(DrawContext  context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!ShieldDelayConfig.enabled) return;

        int shiledDelayTicks = ShielddelayindicatorClient.shiledDelayTicks;;
        if (shiledDelayTicks != -1) {


            String text = String.valueOf(shiledDelayTicks);
            int argb = ColorHelper.getArgb(255,85,85);

            if (shiledDelayTicks == 3 || shiledDelayTicks == 2 || shiledDelayTicks == 1) {
                argb = ColorHelper.getArgb(255,255,85);
            }

            if (shiledDelayTicks == 0) {
                argb = ColorHelper.getArgb(	85, 255, 85);
                text = "✔";
            }

            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            int x = screenWidth / 2;
            int y = screenHeight / 2;


            context.drawText(
                    client.textRenderer,
                    text,
                    x - client.textRenderer.getWidth(text)/2 + ShieldDelayConfig.offsetX,
                    y + ShieldDelayConfig.offsetY,
                    argb,
                    true
            );


        }
    }

}
