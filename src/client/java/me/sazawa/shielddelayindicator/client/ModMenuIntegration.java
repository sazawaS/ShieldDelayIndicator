package me.sazawa.shielddelayindicator.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {



    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> new ConfigScreen(parent);
    }

    static class ConfigScreen extends Screen {
        private final Screen parent;
        private int centerX;

        private TextFieldWidget xOffsetField;
        private TextFieldWidget yOffsetField;

        protected ConfigScreen(Screen parent) {
            super(Text.literal("Shield Delay Indicator"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            centerX = width / 2;

            // Enable toggle
            addDrawableChild(ButtonWidget.builder(
                    getToggleText(),
                    button -> {
                        ShieldDelayConfig.enabled = !ShieldDelayConfig.enabled;
                        button.setMessage(getToggleText());
                    }
            ).dimensions(centerX - 75, height / 2 - 40, 150, 20).build());

            // X Offset field
            xOffsetField = new TextFieldWidget(
                    textRenderer,
                    centerX - 10,
                    height / 2 - 10,
                    150 - 65,
                    20,
                    Text.literal("X Offset")
            );
            xOffsetField.setText(String.valueOf(ShieldDelayConfig.offsetX));
            xOffsetField.setChangedListener(this::onXChanged);
            addDrawableChild(xOffsetField);

            // Y Offset field
            yOffsetField = new TextFieldWidget(
                    textRenderer,
                    centerX - 10,
                    height / 2 + 20,
                    150 - 65,
                    20,
                    Text.literal("Y Offset")
            );
            yOffsetField.setText(String.valueOf(ShieldDelayConfig.offsetY));
            yOffsetField.setChangedListener(this::onYChanged);
            addDrawableChild(yOffsetField);
        }

        @Override
        public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);

            context.drawTextWithShadow(
                    textRenderer,
                    Text.literal("X Offset"),
                    centerX - 75,
                    height / 2 - 4,
                    0xFFFFFF
            );

            context.drawTextWithShadow(
                    textRenderer,
                    Text.literal("Y Offset"),
                    centerX - 75,
                    height / 2 + 26,
                    0xFFFFFF
            );
        }


        private void onXChanged(String text) {
            try {
                ShieldDelayConfig.offsetX = Integer.parseInt(text);
            } catch (NumberFormatException ignored) {
            }
        }

        private void onYChanged(String text) {
            try {
                ShieldDelayConfig.offsetY = Integer.parseInt(text);
            } catch (NumberFormatException ignored) {
            }
        }

        private Text getToggleText() {
            return Text.literal(
                    "Enabled: " + (ShieldDelayConfig.enabled ? "ON" : "OFF")
            );
        }

        @Override
        public void close() {
            client.setScreen(parent);
        }
    }
}

