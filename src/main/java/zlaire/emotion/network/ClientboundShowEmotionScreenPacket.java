package zlaire.emotion.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import zlaire.emotion.player.States;
import zlaire.emotion.player.beforeChestStates;
import zlaire.emotion.screen.EmotionScreen;
import zlaire.emotion.screen.OpenGuI;
import zlaire.emotion.screen.sendInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ClientboundShowEmotionScreenPacket{
    private String event;

    private float Health;
    private int ArmorValue;
    private int FoodLevel;
    private Map<String, Integer> InventoryMap;
    private float oldHealth;
    private int oldArmorValue;
    private int oldFoodLevel;

    private int ifChest=0;

    private Map<String, Integer> ChestInventory;
    private Map<String, Integer> oldChestInventory;
    private Map<String, Integer> oldInventoryMap;
    private String target="Null";
    public ClientboundShowEmotionScreenPacket() {
    }
    public ClientboundShowEmotionScreenPacket(String event) {
        this.event=event;
    }
    public ClientboundShowEmotionScreenPacket(String event, States state) {
        this.event=event;
        this.Health=States.getHealth();
        this.ArmorValue=States.getArmorValue();
        this.FoodLevel=States.getFoodLevel();
        this.InventoryMap=new HashMap<>();
        this.InventoryMap.putAll(States.getInventoryMap());
        this.oldHealth=States.getOldHealth();
        this.oldArmorValue=States.getOldArmorValue();
        this.oldFoodLevel=States.getOldFoodLevel();
        this.oldInventoryMap=new HashMap<>();
        if(States.getInventoryMap()!=null){
            this.oldInventoryMap.putAll(States.getOldInventoryMap());
        }
    }
    public ClientboundShowEmotionScreenPacket(String event, States state, beforeChestStates bcstate) {
        this.event=event;
        this.Health=States.getHealth();
        this.ArmorValue=States.getArmorValue();
        this.FoodLevel=States.getFoodLevel();
        this.InventoryMap=new HashMap<>();
        this.InventoryMap.putAll(States.getInventoryMap());
        this.oldHealth= beforeChestStates.getHealth();
        this.oldArmorValue= beforeChestStates.getArmorValue();
        this.oldFoodLevel= beforeChestStates.getFoodLevel();
        this.oldInventoryMap=new HashMap<>();
        if(States.getInventoryMap()!=null){
            this.oldInventoryMap.putAll(beforeChestStates.getInventoryMap());
        }
    }
    public ClientboundShowEmotionScreenPacket(String event, States state, beforeChestStates bcstate, Map<String, Integer> preChestMap,Map<String, Integer> ChestMap) {
        this.event=event;
        this.Health=States.getHealth();
        this.ArmorValue=States.getArmorValue();
        this.FoodLevel=States.getFoodLevel();
        this.InventoryMap=new HashMap<>();
        this.InventoryMap.putAll(States.getInventoryMap());
        this.oldHealth= beforeChestStates.getHealth();
        this.oldArmorValue= beforeChestStates.getArmorValue();
        this.oldFoodLevel= beforeChestStates.getFoodLevel();
        this.oldInventoryMap=new HashMap<>();
        this.ifChest=1;
        if(States.getInventoryMap()!=null){
            this.oldInventoryMap.putAll(beforeChestStates.getInventoryMap());
        }
        this.oldChestInventory=new HashMap<>();
        this.oldChestInventory.putAll(preChestMap);
        this.ChestInventory=new HashMap<>();
            this.ChestInventory.putAll(ChestMap);

    }
    public ClientboundShowEmotionScreenPacket(String event, States state,String tar) {
        this.event=event;
        this.Health=States.getHealth();
        this.ArmorValue=States.getArmorValue();
        this.FoodLevel=States.getFoodLevel();
        this.InventoryMap=new HashMap<>();
        this.InventoryMap.putAll(States.getInventoryMap());
        this.oldHealth=States.getOldHealth();
        this.oldArmorValue=States.getOldArmorValue();
        this.oldFoodLevel=States.getOldFoodLevel();
        this.oldInventoryMap=new HashMap<>();
        if(States.getInventoryMap()!=null){
            this.oldInventoryMap.putAll(States.getOldInventoryMap());
        }
        this.target=tar;
    }

    public ClientboundShowEmotionScreenPacket(FriendlyByteBuf buffer) {
        this.event=buffer.readUtf();
        this.Health=buffer.readFloat();
        this.ArmorValue=buffer.readInt();
        this.FoodLevel=buffer.readInt();
        this.InventoryMap=buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt);
        this.oldHealth=buffer.readFloat();
        this.oldArmorValue=buffer.readInt();
        this.oldFoodLevel=buffer.readInt();
        this.ifChest=buffer.readInt();
        this.oldInventoryMap=buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt);
        this.target=buffer.readUtf();
        if(ifChest==1){
            this.oldChestInventory=buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt);
            this.ChestInventory=buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt);
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(event);
        buffer.writeFloat(Health);
        buffer.writeInt(ArmorValue);
        buffer.writeInt(FoodLevel);
        buffer.writeMap(InventoryMap,FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
        buffer.writeFloat(oldHealth);
        buffer.writeInt(oldArmorValue);
        buffer.writeInt(oldFoodLevel);
        buffer.writeInt(ifChest);
        buffer.writeMap(oldInventoryMap,FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
        buffer.writeUtf(target);
        if(ifChest==1) {
            buffer.writeMap(oldChestInventory, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
            buffer.writeMap(ChestInventory, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
            //player state
            JsonObject state = new JsonObject();
            JsonArray Inventory =new JsonArray();
            JsonObject jsonObject = new JsonObject();
            for (Map.Entry<String, Integer> entry : InventoryMap.entrySet()) {
                String name = entry.getKey();
                int count = entry.getValue();
                jsonObject.addProperty(name,count);
            }
            Inventory.add(jsonObject);

            JsonArray oldInventory =new JsonArray();
            JsonObject oldjsonObject = new JsonObject();
            for (Map.Entry<String, Integer> entry : oldInventoryMap.entrySet()) {
                String name = entry.getKey();
                int count = entry.getValue();
                oldjsonObject.addProperty(name,count);
            }

            oldInventory.add(oldjsonObject);


            if(ifChest==1){
                JsonArray oldChest =new JsonArray();
                JsonObject oldChestObject = new JsonObject();
                if(!oldChestInventory.isEmpty()){
                    for (Map.Entry<String, Integer> entry : oldChestInventory.entrySet()) {
                        String name = entry.getKey();
                        int count = entry.getValue();
                        oldChestObject.addProperty(name,count);
                    }
                }
                oldChest.add(oldChestObject);
                JsonArray Chest =new JsonArray();
                JsonObject ChestObject = new JsonObject();
                if(!ChestInventory.isEmpty()){
                    for (Map.Entry<String, Integer> entry : ChestInventory.entrySet()) {
                        String name = entry.getKey();
                        int count = entry.getValue();
                        ChestObject.addProperty(name,count);
                    }
                }
                Chest.add(ChestObject);
                state.add("ChestInventory",Chest);
                state.add("PreviousChestInventory",oldChest);
            }


            state.addProperty("Health",Health);
            state.addProperty("PreviousHealth",oldHealth);
            state.addProperty("ArmorValue",ArmorValue);
            state.addProperty("PreviousArmorValue",oldArmorValue);
            state.addProperty("FoodLevel",FoodLevel);
            state.addProperty("PreviousFoodLevel",oldFoodLevel);
            state.add("Inventory",Inventory);
            state.add("PreviousInventory",oldInventory);

            ctx.get().enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> () -> {
                    //OpenGuI.OpenGUI(event,state);
                    sendInfo.SendInfo(event,state,target);
                });
            });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
