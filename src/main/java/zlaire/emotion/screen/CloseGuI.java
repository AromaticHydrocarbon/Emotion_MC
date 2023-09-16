package zlaire.emotion.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;

public class CloseGuI {
    public static void CloseGUI() {
        Minecraft.getInstance().popGuiLayer();
    }
}
