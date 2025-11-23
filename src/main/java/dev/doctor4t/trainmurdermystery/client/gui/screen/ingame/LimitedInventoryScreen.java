package dev.doctor4t.trainmurdermystery.client.gui.screen.ingame;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.StoreRenderer;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import dev.doctor4t.trainmurdermystery.util.StoreBuyPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class LimitedInventoryScreen extends LimitedHandledScreen<PlayerScreenHandler> {
    public static final Identifier BACKGROUND_TEXTURE = TMM.id("textures/gui/container/limited_inventory.png");
    public static final @NotNull Identifier ID = TMM.id("textures/gui/game.png");
    public final ClientPlayerEntity player;

    public LimitedInventoryScreen(@NotNull ClientPlayerEntity player) {
        super(player.playerScreenHandler, player.getInventory(), Text.empty());
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();
        if (!GameWorldComponent.KEY.get(this.player.getWorld()).canUseKillerFeatures(player)) return;
        var entries = GameConstants.SHOP_ENTRIES;
        int apart = 38;
        var x = this.width / 2 - entries.size() * apart / 2 + 9;
        var y = this.y - 46;
        for (var i = 0; i < entries.size(); i++)
            this.addDrawableChild(new StoreItemWidget(this, x + apart * i, y, entries.get(i), i));
    }

    @Override
    protected void drawBackground(@NotNull DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        context.getMatrices().push();
        context.getMatrices().translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight(), 0);
        float scale = 0.28f;
        context.getMatrices().scale(scale, scale, 1f);
        int height = 254;
        int width = 497;
        context.getMatrices().translate(0, -230, 0);
        int xOffset = 0;
        int yOffset = 0;
        context.drawTexturedQuad(ID, (int) (xOffset - width / 2f), (int) (xOffset + width / 2f), (int) (yOffset - height / 2f), (int) (yOffset + height / 2f), 0, 0, 1f, 0, 1f, 1f, 1f, 1f, 1f);
        context.getMatrices().pop();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        StoreRenderer.renderHud(this.textRenderer, this.player, context, delta);
    }

    public static class StoreItemWidget extends ButtonWidget {
        public final LimitedInventoryScreen screen;
        public final ShopEntry entry;

        public StoreItemWidget(LimitedInventoryScreen screen, int x, int y, @NotNull ShopEntry entry, int index) {
            super(x, y, 16, 16, entry.stack().getName(), (a) -> ClientPlayNetworking.send(new StoreBuyPayload(index)), DEFAULT_NARRATION_SUPPLIER);
            this.screen = screen;
            this.entry = entry;
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderWidget(context, mouseX, mouseY, delta);
            context.drawGuiTexture(entry.type().getTexture(), this.getX() - 7, this.getY() - 7, 30, 30);
//            context.drawGuiTexture(TMM.id("gui/shop_slot"), this.getX() - 7, this.getY() - 7, 30, 30);
            context.drawItem(this.entry.stack(), this.getX(), this.getY());
            if (this.isHovered()) {
                this.screen.renderLimitedInventoryTooltip(context, this.entry.stack());
                drawShopSlotHighlight(context, this.getX(), this.getY(), 0);
            }
            var price = Text.literal(this.entry.price() + "\uE781");
            context.drawTooltip(this.screen.textRenderer, price, this.getX() - 4 - this.screen.textRenderer.getWidth(price) / 2, this.getY() - 9);
        }

        private void drawShopSlotHighlight(DrawContext context, int x, int y, int z) {
            int color = 0x90FFBF49;
//            context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, color, color, z);
            context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + 16, y + 14, color, color, z);
            context.fillGradient(RenderLayer.getGuiOverlay(), x, y + 14, x + 15, y + 15, color, color, z);
            context.fillGradient(RenderLayer.getGuiOverlay(), x, y + 15, x + 14, y + 16, color, color, z);
        }

        @Override
        public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        }
    }
}