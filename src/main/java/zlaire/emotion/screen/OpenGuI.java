package zlaire.emotion.screen;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;

import java.util.Map;


public class OpenGuI {
    public static void OpenGUI() {
        Minecraft.getInstance().setScreen(new EmotionScreen(new TextComponent("test")));
    }
    public static void OpenGUI(int mode) {
        Minecraft.getInstance().setScreen(new EmotionScreen(new TextComponent("test")));
    }
    public static void OpenGUI(int mode,String event) {
        Minecraft.getInstance().setScreen(new EmotionScreen(new TextComponent("test"),event));
    }
    public static void OpenGUI(int mode, String event,JsonObject state) {
        Minecraft.getInstance().setScreen(new EmotionScreen(new TextComponent("test"),event,state));
    }

}
