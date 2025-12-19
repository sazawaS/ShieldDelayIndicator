package me.sazawa.shielddelayindicator.mixin.client;

import me.sazawa.shielddelayindicator.client.ShielddelayindicatorClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(
            method = "onMouseButton",
            at = @At("HEAD")
    )
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {

            if (action == GLFW.GLFW_PRESS) {
                ShielddelayindicatorClient.isRightClickPressed = true;
            } if (action == GLFW.GLFW_RELEASE) {
                ShielddelayindicatorClient.isRightClickPressed = false;
            }
        }
    }
}
