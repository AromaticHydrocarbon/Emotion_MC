package zlaire.emotion.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
//
//public class EmotionSelectScreen extends AbstractContainerScreen<EmotionSelectMenu> {
//    Button button;
//    public EmotionSelectScreen(EmotionSelectMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
//        super(pMenu, pPlayerInventory, pTitle);
//    }
//
//    @Override
//    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        int x = (width - imageWidth) / 2;
//        int y = (height - imageHeight) / 2;
//
//        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
//    }
//}
