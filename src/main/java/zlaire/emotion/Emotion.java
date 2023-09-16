package zlaire.emotion;

import com.mojang.logging.LogUtils;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


import zlaire.emotion.init.EventHandler;
import zlaire.emotion.init.PacketHandler;
import zlaire.emotion.setup.Registration;
import zlaire.emotion.world.structure.ModStructures;

import java.util.stream.Collectors;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("emotion")
public class Emotion {

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Emotion() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);


        // 通常在模组主类的构造方法注册事件监听器
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        //MinecraftForge.EVENT_BUS.addListener(EventHandler::jumpIntoWater);


        //gui
        //ModMenuTypes.register(bus);
        // Add to ClientSetup
        //MenuScreens.register(ModMenuTypes.EMOTION_SELECT_MENU.get(), EmotionSelectScreen::new);

        //添加结构
        ModStructures.register(bus);

        Registration.init();

        // 实例注册到Forge总线中
        //MinecraftForge.EVENT_BUS.register(new MyForgeEventHandler());
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


//    public static class StateProvider extends BlockStateProvider {
//        public StateProvider(DataGenerator gen, ExistingFileHelper helper) {
//            super(gen, "emotion", helper);
//        }
//
//        @Override
//        protected void registerStatesAndModels() {
//            this.simpleBlock(SULFUR_BLOCK.get(), this.cubeAll(SULFUR_BLOCK.get()));
//            this.simpleBlockItem(SULFUR_BLOCK.get(), this.cubeAll(SULFUR_BLOCK.get()));
//        }
//    }



    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        event.enqueueWork(PacketHandler::init);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("emotion", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
