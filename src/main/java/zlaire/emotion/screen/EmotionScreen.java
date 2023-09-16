package zlaire.emotion.screen;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zlaire.emotion.http.Http;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@OnlyIn(Dist.CLIENT)
public class EmotionScreen extends Screen {
    static Minecraft minecraft = Minecraft.getInstance();
    Button neutral;
    Button joyful;
    Button distressed;
    Button hopeful;
    Button fearful;

    String event;

    JsonObject state;

    /** mode
     * 1:monster meet
     * 2n:see chest
     * 3n:open chest
     *              */
    int mode;

    String content = "What is your emotion at the moment?";

    public EmotionScreen(Component pTitle) {
        super(pTitle);
    }

    public EmotionScreen(Component pTitle,String event) {
        super(pTitle);
        this.event=event;
    }
    public EmotionScreen(Component pTitle,String event,JsonObject state) {
        super(pTitle);
        this.event=event;
        this.state=state;
    }



    @Override
    protected void init() {
        this.neutral = new Button(this.width / 2-40, 60, 80, 20, new TextComponent("neutral"), (button) -> {
            minecraft.popGuiLayer();
//            if(mode==1){
//                minecraft.player.sendMessage(new TextComponent("The player doesn't care about his blood level."), Util.NIL_UUID);
//                //ShowTitle.showtitle(minecraft,"The player","doesn't care about his blood level.");
//            }
//            else if(mode==21||mode==22){
//                minecraft.player.sendMessage(new TextComponent("The player doesn't care about the objects in the chest."), Util.NIL_UUID);
//                //ShowTitle.showtitle(minecraft,"The player","doesn't care about the objects in the chest.");
//            }
//            else if(mode==31){
//                minecraft.player.sendMessage(new TextComponent("The player does not care about the weapon in the chest."), Util.NIL_UUID);
//                //ShowTitle.showtitle(minecraft,"The player","does not care about the weapon in the chest.");
//            }
//            else if(mode==32){
//                minecraft.player.sendMessage(new TextComponent("The player does not care about the foods in the chest."), Util.NIL_UUID);
//                //ShowTitle.showtitle(minecraft,"The player","does not care about the foods in the chest.");
//            }
            JsonObject object = new JsonObject();
            object.addProperty("Emotion","neutral");
            object.addProperty("Event",this.event);
            object.add("State",state);
            String result=sendHttpMessage(object);
            minecraft.player.sendMessage(new TextComponent(result), Util.NIL_UUID);
        });
        this.joyful = new Button(this.width / 2 -120, 100, 80, 20, new TextComponent("joyful"), (button) -> {
            minecraft.popGuiLayer();
//            if(mode==1){
//                minecraft.player.sendMessage(new TextComponent("The player doesn't care about his blood level."), Util.NIL_UUID);
//                //ShowTitle.showtitle(minecraft,"The player","doesn't care about his blood level.");
//            }
//            else if(mode==31){
//                minecraft.player.sendMessage(new TextComponent("The player cares about the weapon in the chest."), Util.NIL_UUID);
//                //ShowTitle.showtitle(minecraft,"The player","cares about the weapon in the chest.");
//            }
//            else if(mode==32){
//                minecraft.player.sendMessage(new TextComponent("The player cares about the foods in the chest."), Util.NIL_UUID);
//                //ShowTitle.showtitle(minecraft,"The player","cares about the foods in the chest.");
//            }
            JsonObject object = new JsonObject();
            object.addProperty("Emotion","joyful");
            object.addProperty("Event",this.event);
            object.add("State",state);
            String result=sendHttpMessage(object);
            minecraft.player.sendMessage(new TextComponent(result), Util.NIL_UUID);
        });
        this.distressed = new Button(this.width / 2 + 40, 100, 80, 20, new TextComponent("distressed"), (button) -> {
            minecraft.popGuiLayer();
            JsonObject object = new JsonObject();
            object.addProperty("Emotion","distressed");
            object.addProperty("Event",this.event);
            object.add("State",state);
            String result=sendHttpMessage(object);
            minecraft.player.sendMessage(new TextComponent(result), Util.NIL_UUID);
        });
        this.hopeful = new Button(this.width / 2 - 120, 200, 80, 20, new TextComponent("hopeful"), (button) -> {
            minecraft.popGuiLayer();
//            if(mode==21||mode==22){
//                minecraft.player.sendMessage(new TextComponent("The player cares about the objects in the chest."), Util.NIL_UUID);
//                //ShowTitle.showtitle(minecraft,"The player","cares about the objects in the chest.");
//            }
            JsonObject object = new JsonObject();
            object.addProperty("Emotion","hopeful");
            object.addProperty("Event",this.event);
            object.add("State",state);
            String result=sendHttpMessage(object);
            minecraft.player.sendMessage(new TextComponent(result), Util.NIL_UUID);
        });
        this.fearful = new Button(this.width / 2 + 40, 200, 80, 20, new TextComponent("fearful"), (button) -> {
            minecraft.popGuiLayer();
//            if(mode==1){
//                minecraft.player.sendMessage(new TextComponent("The player cares about his blood level."), Util.NIL_UUID);
//                //ShowTitle.showtitle(minecraft,"The player","cares about his blood level.");
//            }
            JsonObject object = new JsonObject();
            object.addProperty("Emotion","fearful");
            object.addProperty("Event",this.event);
            object.add("State",state);
            String result=sendHttpMessage(object);
            minecraft.player.sendMessage(new TextComponent(result), Util.NIL_UUID);
        });
        this.addWidget(neutral);
        this.addWidget(joyful);
        this.addWidget(distressed);
        this.addWidget(hopeful);
        this.addWidget(fearful);
        super.init();
    }

    private String sendHttpMessage(JsonObject object) {
        String result="No Connection";
        try {
            result=Http.testPost(object);
        } catch (IOException | InterruptedException | ExecutionException e) {
            result="No Connection";
        }
        return result;
    }


    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //this.minecraft.getTextureManager().bindTexture(OBSIDIAN_FIRST_GUI_TEXTURE);
        //int textureWidth = 208;
        //int textureHeight = 156;
        //this.blit(this.width / 2 - 150, 10, 0, 0, 300, 200, textureWidth, textureHeight);
        drawString(pPoseStack,this.font, content, this.width / 2 - 80, 30, 0xeb0505);

        this.neutral.render(pPoseStack,pMouseX, pMouseY, pPartialTick);
        this.joyful.render(pPoseStack,pMouseX, pMouseY, pPartialTick);
        this.distressed.render(pPoseStack,pMouseX, pMouseY, pPartialTick);
        this.hopeful.render(pPoseStack,pMouseX, pMouseY, pPartialTick);
        this.fearful.render(pPoseStack,pMouseX, pMouseY, pPartialTick);
        super.render(pPoseStack,pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
