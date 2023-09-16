package zlaire.emotion.screen;

import com.google.gson.JsonObject;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import zlaire.emotion.http.Http;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class sendInfo {
    public static void SendInfo(String event, JsonObject state,String target) {
        JsonObject object = new JsonObject();
        object.addProperty("Event",event);
        object.add("State",state);
        if(!target.equals("Null"))
            object.addProperty("Target",target);
        String result=sendHttpMessage(object);
        Minecraft.getInstance().player.sendMessage(new TextComponent(result), Util.NIL_UUID);
    }

    private static String sendHttpMessage(JsonObject object) {
        String result="No Connection";
        try {
            result= Http.testPost(object);
        } catch (IOException | InterruptedException | ExecutionException e) {
            result="No Connection";
        }
        return result;
    }

}
