package zlaire.emotion.screen;

import net.minecraft.client.Minecraft;

import java.util.Objects;

public class ShowTitle {
    static void showtitle(Minecraft minecraft, String str1, String str2){
        String command="title @a title {\"text\":\""+str1+"\"}";
        String command1="title @a subtitle {\"text\":\""+str2+"\"}";
        Objects.requireNonNull(minecraft.player.getServer()).getCommands().performCommand(minecraft.player.getServer().createCommandSourceStack(),command);
        Objects.requireNonNull(minecraft.player.getServer()).getCommands().performCommand(minecraft.player.getServer().createCommandSourceStack(),command1);
    }
}
