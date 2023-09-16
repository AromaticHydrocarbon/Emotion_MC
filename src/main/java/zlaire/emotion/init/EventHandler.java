package zlaire.emotion.init;
import com.alibaba.fastjson.JSONObject;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import zlaire.emotion.network.ClientboundShowEmotionScreenPacket;
import zlaire.emotion.player.States;
import zlaire.emotion.player.beforeChestStates;
import zlaire.emotion.tools.myItem;

import java.awt.*;
import java.util.*;

import static zlaire.emotion.http.Http.testPost;
import static zlaire.emotion.player.States.forceChange;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    static Minecraft minecraft = Minecraft.getInstance();
    static Set<myItem> find_chest_set = new HashSet<myItem>();
    //static Set<myItem> open_chest_set = new HashSet<myItem>();
    static Set<Integer> monster_set=new HashSet<Integer>();
    static boolean opengui=false;

    static States states;
    static beforeChestStates bcStates;
    static Map<String, Integer> preChestMap = new HashMap<>();
    static Map<String, Integer> ChestMap  = new HashMap<>();
    static boolean meetMonster = false;
    private static ItemStack[] previousInventory; // Store the previous inventory state
    static BlockEntity blockEntity;


    static int chest_num=1;
    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getPlayer();
        player.sendMessage(new TextComponent("Player logged in"), Util.NIL_UUID);
        String command="title @a times 17 1 2";
        Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command);
    }
    @SubscribeEvent
    public static void onEvent(EntityJoinWorldEvent event) {
        //ItemRegistry.writeAllEntityNamesToFile("entity_names.txt");
        if ((event.getEntity() instanceof Player player)) {
            //init
            opengui=false;
            find_chest_set = new HashSet<myItem>();
            //open_chest_set = new HashSet<myItem>();
            monster_set=new HashSet<Integer>();
            chest_num=1;
            meetMonster=false;
            preChestMap.clear();
            ChestMap.clear();
            beforeChestStates bcStates=new beforeChestStates();
            //get states
            float Health = player.getHealth();
            int ArmorValue=player.getArmorValue();
            int FoodLevel=player.getFoodData().getFoodLevel();
            Map<String, Integer> inventoryMap = new HashMap<>();
            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                ItemStack stack = player.getInventory().getItem(slot);
                // Use the item stack as needed
                // You can check its properties, manipulate, or perform any other operations
                if (!stack.isEmpty()) {
                    System.out.println("Item in slot " + slot + ": " + stack.getDisplayName().getString());
                    int count = stack.getCount();
                    String name = stack.getDisplayName().getString();
                    if(inventoryMap.containsKey(name)){
                        count += inventoryMap.get(name);
                        inventoryMap.put(name,count);
                    }
                    else{
                        inventoryMap.put(name,count);
                    }
                }
            }
            states=new States(Health,ArmorValue,FoodLevel,inventoryMap);
            LogManager.getLogger().info("Join!");
        }
    }
    @SubscribeEvent
    public static void monsterSeePlayer(LivingChangeTargetEvent event) throws InterruptedException {
        LivingEntity target = event.getNewTarget();
        if(target instanceof Player player){
            var source=event.getEntityLiving();
            int id=source.getId();
            if (!monster_set.contains(id)){
                monster_set.add(id);
                String sourceName=source.getDisplayName().getString();
                PacketHandler.INSTANCE.send(
                        PacketDistributor.ALL.noArg(),
                        new ClientboundShowEmotionScreenPacket("encounterMonsterEvent",states,sourceName));
                float Health = player.getHealth();
                int ArmorValue=player.getArmorValue();
                int FoodLevel=player.getFoodData().getFoodLevel();
                Map<String, Integer> inventoryMap = new HashMap<>();
                for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                    ItemStack stack = player.getInventory().getItem(slot);
                    // Use the item stack as needed
                    // You can check its properties, manipulate, or perform any other operations
                    if (!stack.isEmpty()) {
                        //System.out.println("Item in slot " + slot + ": " + stack.getDisplayName().getString());
                        int count = stack.getCount();
                        String name = stack.getDisplayName().getString();
                        if(inventoryMap.containsKey(name)){
                            count += inventoryMap.get(name);
                            inventoryMap.put(name,count);
                        }
                        else{
                            inventoryMap.put(name,count);
                        }
                    }
                }
                States.change(Health,ArmorValue,FoodLevel,inventoryMap);
            }
        }
    }

    @SubscribeEvent
    public static void getPlayerStates(TickEvent.PlayerTickEvent event) {
        if(!opengui){
            Player player = event.player;
            //get player states
            //health
            float Health = player.getHealth();
            int ArmorValue = player.getArmorValue();
            int FoodLevel = player.getFoodData().getFoodLevel();
            Map<String, Integer> inventoryMap = new HashMap<>();
            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                ItemStack stack = player.getInventory().getItem(slot);
                // Use the item stack as needed
                // You can check its properties, manipulate, or perform any other operations
                if (!stack.isEmpty()) {
                    //System.out.println("Item in slot " + slot + ": " + stack.getDisplayName().getString());
                    int count = stack.getCount();
                    String name = stack.getDisplayName().getString();
                    if (inventoryMap.containsKey(name)) {
                        count += inventoryMap.get(name);
                        inventoryMap.put(name, count);
                    } else {
                        inventoryMap.put(name, count);
                    }
                }
            }
            States.change(Health, ArmorValue, FoodLevel, inventoryMap);
        }
    }

    @SubscribeEvent
    public static void watchAt(TickEvent.PlayerTickEvent event) {
            Player player = event.player;
            var level = player.getLevel();
            double rayLength = 100.0;
            Vec3 playerRotation = player.getViewVector(0);
            Vec3 rayPath = playerRotation.scale(rayLength);
            //RAY START AND END POINTS
            Vec3 from = player.getEyePosition(0);
            Vec3 to = from.add(rayPath);
            //CREATE THE RAY
            ClipContext rayCtx = new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, null);
            //CAST THE RAY
            HitResult rayHit = level.clip(rayCtx);

            //CHECK THE RESULTS
            if (rayHit.getType() == HitResult.Type.MISS) {
                //IF RAY MISSED
                //player.sendMessage(new TextComponent("nothing"),Util.NIL_UUID);
            } else {
                //IF RAY HIT SOMETHING
                Vec3 hitLocation = rayHit.getLocation();
                BlockPos pos = new BlockPos(hitLocation.x, hitLocation.y, hitLocation.z);
                var block_state = level.getBlockState(pos);
                Block block = block_state.getBlock();
                String name = block.getDescriptionId();
                myItem location = new myItem(pos.getX(), pos.getY(), pos.getZ());
                if (!find_chest_set.contains(location) && name.equals("block.minecraft.chest")) {
                    find_chest_set.add(location);
                    //change
                    float Health = player.getHealth();
                    int ArmorValue = player.getArmorValue();
                    int FoodLevel = player.getFoodData().getFoodLevel();
                    Map<String, Integer> inventoryMap = new HashMap<>();
                    for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                        ItemStack stack = player.getInventory().getItem(slot);
                        // Use the item stack as needed
                        // You can check its properties, manipulate, or perform any other operations
                        if (!stack.isEmpty()) {
                            //System.out.println("Item in slot " + slot + ": " + stack.getDisplayName().getString());
                            int count = stack.getCount();
                            String itemName = stack.getDisplayName().getString();
                            if (inventoryMap.containsKey(itemName)) {
                                count += inventoryMap.get(itemName);
                                inventoryMap.put(itemName, count);
                            } else {
                                inventoryMap.put(itemName, count);
                            }
                        }
                    }
                    States.change(Health, ArmorValue, FoodLevel, inventoryMap);
                    //if(block instanceof ChestBlock chestBlock)
                    try {
                        Thread.sleep(500);//单位：毫秒
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    PacketHandler.INSTANCE.send(
                            PacketDistributor.ALL.noArg(),
                            new ClientboundShowEmotionScreenPacket("watchChestEvent", states));
                }
            }
    }
    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.RightClickBlock event) {
        if(event.getSide()== LogicalSide.SERVER){
            var target=event.getHitVec().getBlockPos();
            Player player = event.getPlayer();
            Block block= player.getLevel().getBlockState(target).getBlock();
            String name= block.getDescriptionId();
            //myItem location=new myItem(target.getX(),target.getY(),target.getZ());
            //if(!open_chest_set.contains(location)&&name.equals("block.minecraft.chest")){
                //open_chest_set.add(location);
            if(name.equals("block.minecraft.chest")){
                blockEntity = event.getWorld().getBlockEntity(target);
                if (blockEntity instanceof Container con) {
                    for(int slot = 0; slot < con.getContainerSize(); slot++){
                        ItemStack stack=con.getItem(slot);
                        if (!stack.isEmpty()) {
                            //System.out.println("Item in slot " + slot + ": " + stack.getDisplayName().getString());
                            int count = stack.getCount();
                            String itemname = stack.getDisplayName().getString();
                            if (preChestMap.containsKey(itemname)) {
                                count += preChestMap.get(itemname);
                                preChestMap.put(itemname, count);
                            } else {
                                preChestMap.put(itemname, count);
                            }
                        }
                    }
                }
                System.out.print("1:");
                System.out.println(preChestMap);
                bcStates.change(states.getHealth(),states.getArmorValue(),states.getFoodLevel(),states.getInventoryMap());
                opengui=true;
//                if(block instanceof TrappedChestBlock trappedchest) {
//                    while(Mth.clamp(ChestBlockEntity.getOpenCount(player.getLevel(), target), 0, 15)!=0){}
//                }
            }
        }
    }
    @SubscribeEvent
    public static void ChestGuiClosed(ScreenEvent.KeyboardKeyEvent event) {
        Player player=minecraft.player;
        if(player.getLevel().isClientSide()){
            if (opengui) {
                int key = event.getKeyCode();
                if (key == 256 || key == 69) {
                    //chestitem
                    if (blockEntity instanceof Container con) {
                        for(int slot = 0; slot < con.getContainerSize(); slot++){
                            ItemStack stack=con.getItem(slot);
                            if (!stack.isEmpty()) {
                                //System.out.println("Item in slot " + slot + ": " + stack.getDisplayName().getString());
                                int count = stack.getCount();
                                String itemname = stack.getDisplayName().getString();
                                if (ChestMap.containsKey(itemname)) {
                                    count += ChestMap.get(itemname);
                                    ChestMap.put(itemname, count);
                                } else {
                                    ChestMap.put(itemname, count);
                                }
                            }
                        }
                    }

                    System.out.print("2:");
                    System.out.println(preChestMap);
                    //change
                    updateInfo(player);
                    PacketHandler.INSTANCE.send(
                            PacketDistributor.ALL.noArg(),
                            new ClientboundShowEmotionScreenPacket("closeChestEvent", states,bcStates,preChestMap,ChestMap));
                    opengui = false;
                    chest_num += 1;
                    ChestMap.clear();
                    preChestMap.clear();
                }
            }
        }
    }
    @SubscribeEvent
    public static void playerAttack(AttackEntityEvent event) {
        if(event.getPlayer().getLevel().isClientSide()) {
            Player player=event.getPlayer();
            updateInfo(player);
            var living = event.getTarget();
            String living_name = living.getName().getString();
            PacketHandler.INSTANCE.send(
                    PacketDistributor.ALL.noArg(),
                    new ClientboundShowEmotionScreenPacket("playerAttackEvent", states, living_name));
        }
    }

    @SubscribeEvent
    public static void playerKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player) {

            var living=event.getEntityLiving();
            String living_name = living.getName().getString();
            PacketHandler.INSTANCE.send(
                    PacketDistributor.ALL.noArg(),
                    new ClientboundShowEmotionScreenPacket("playerKillEvent", states,living_name));
        }
    }
    @SubscribeEvent
    public static void pickUpItem(PlayerEvent.ItemPickupEvent event) {
        /*
          changestate
         */

        Player player = event.getPlayer();
        updateInfo(player);
        PacketHandler.INSTANCE.send(
                PacketDistributor.ALL.noArg(),
                new ClientboundShowEmotionScreenPacket("pickUpItemEvent", states));
//        Player player = event.getPlayer();
//        var item= event.getItem();
//        String item_name = item.getItem().getDescriptionId();
//        if(item.getItem().isEdible()){
//            String command="title @a title {\"text\":\"Are you joyful\"}";
//            String command1="title @a subtitle {\"text\":\"for having foods in your bag?\"}";
//            String command2="title @a title {\"text\":\"The result of killing\"}";
//            String command3="title @a subtitle {\"text\":\"animals is access to spare food\"}";
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command);
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command1);
//            try
//            {
//                Thread.sleep(2000);//单位：毫秒
//            } catch (Exception e) {}
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command2);
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command3);
//        }
//        else if(item_name.contains("raw_iron")){
//            LogUtils.getLogger().info(item.getDisplayName().getString());
//            String command="title @a title {\"text\":\"Are you joyful\"}";
//            String command1="title @a subtitle {\"text\":\"with getting more iron?\"}";
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command);
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command1);
//        }
//        player.sendMessage(new TextComponent("Item "+item.getDisplayName().getString()+" pick up"), Util.NIL_UUID);
    }
    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        /*
          changestate
         */
        Player player = event.getPlayer();
        updateInfo(player);
        PacketHandler.INSTANCE.send(
                PacketDistributor.ALL.noArg(),
                new ClientboundShowEmotionScreenPacket("breakBlockEvent", states));
//        Player player = event.getPlayer();
//        String command="title @a title {\"text\":\"Are you happy\"}";
//        String command1="title @a subtitle {\"text\":\"for having more wood？\"}";
//
//        String command2="title @a title {\"text\":\"As a result of\"}";
//        String command3="title @a subtitle {\"text\":\"cutting down trees, more wood is obtained.\"}";
//        var block= event.getState().getBlock();
//        String block_name = block.getDescriptionId();
//        if(block_name.contains("log")||block_name.contains("wood"))
//        {
//            PacketHandler.INSTANCE.send(
//                    PacketDistributor.ALL.noArg(),
//                    new ClientboundShowEmotionScreenPacket());
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command);
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command1);
//            try
//            {
//                Thread.sleep(2000);//单位：毫秒
//            } catch (Exception e) {}
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command2);
//            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command3);
//        }
        //minecraft.player.sendMessage(new TextComponent(block_name+" is break"), Util.NIL_UUID);
    }
    @SubscribeEvent
    public static void eatFood(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof Player player){
            if(event.getItem().isEdible()){
                PacketHandler.INSTANCE.send(
                        PacketDistributor.ALL.noArg(),
                        new ClientboundShowEmotionScreenPacket("eatFoodEvent", states));
            }
        }
    }
    @SubscribeEvent
    public static void livingAttack(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            DamageSource source = event.getSource();
            if(source.getEntity()!=null) {
                float damageTaken = event.getAmount();
                float currentHealth = player.getHealth() - damageTaken;
                updateInfo(player, currentHealth);
                String name = source.getEntity().getDisplayName().getString();
                PacketHandler.INSTANCE.send(
                        PacketDistributor.ALL.noArg(),
                        new ClientboundShowEmotionScreenPacket("LivingAttackEvent", states, name));
            }
        }
    }
    public static void updateInfo(Player player){
        float Health = player.getHealth();
        int ArmorValue=player.getArmorValue();
        int FoodLevel=player.getFoodData().getFoodLevel();
        Map<String, Integer> inventoryMap = new HashMap<>();
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            // Use the item stack as needed
            // You can check its properties, manipulate, or perform any other operations
            if (!stack.isEmpty()) {
                //System.out.println("Item in slot " + slot + ": " + stack.getDisplayName().getString());
                int count = stack.getCount();
                String name = stack.getDisplayName().getString();
                if(inventoryMap.containsKey(name)){
                    count += inventoryMap.get(name);
                    inventoryMap.put(name,count);
                }
                else{
                    inventoryMap.put(name,count);
                }
            }
        }
        States.change(Health,ArmorValue,FoodLevel,inventoryMap);
    }
    public static void updateInfo(Player player,float Health){
        int ArmorValue=player.getArmorValue();
        int FoodLevel=player.getFoodData().getFoodLevel();
        Map<String, Integer> inventoryMap = new HashMap<>();
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            // Use the item stack as needed
            // You can check its properties, manipulate, or perform any other operations
            if (!stack.isEmpty()) {
                //System.out.println("Item in slot " + slot + ": " + stack.getDisplayName().getString());
                int count = stack.getCount();
                String name = stack.getDisplayName().getString();
                if(inventoryMap.containsKey(name)){
                    count += inventoryMap.get(name);
                    inventoryMap.put(name,count);
                }
                else{
                    inventoryMap.put(name,count);
                }
            }
        }
        States.change(Health,ArmorValue,FoodLevel,inventoryMap);
    }
    //@SubscribeEvent
    public static void tossItem(ItemTossEvent event) {
        Player player = event.getPlayer();
        var item= event.getEntityItem();
        String item_name = item.getName().getString();
        //player.sendMessage(new TextComponent("Item "+item_name+" toss"), Util.NIL_UUID);
    }

    //@SubscribeEvent
    public static void entityPlace(BlockEvent.EntityPlaceEvent event) {
        var block= event.getPlacedBlock().getBlock();
        String block_name = block.getDescriptionId();
        //minecraft.player.sendMessage(new TextComponent(block_name+" is placed"), Util.NIL_UUID);

    }

    //@SubscribeEvent
    public static void livingDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        var living= event.getEntityLiving();
        String living_name = living.getName().getString();
        String source_name=source.toString();
        //minecraft.player.sendMessage(new TextComponent(living_name+" die from "+source_name), Util.NIL_UUID);
    }




    public static void jumpIntoWater(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        String command1="title @a title {\"text\":\"Are you\"}";
        String command2="title @a subtitle {\"text\":\"hopeful or fearful?\"}";

        if(player.isInWater())
        {
            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command1);
            Objects.requireNonNull(player.getServer()).getCommands().performCommand(player.getServer().createCommandSourceStack(),command2);
        }
    }
}
