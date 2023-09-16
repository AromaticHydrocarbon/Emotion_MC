package zlaire.emotion.setup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import zlaire.emotion.tools.EmotionSelectorA;
import zlaire.emotion.tools.EmotionSelectorB;
import zlaire.emotion.tools.EmotionSelectorC;
import zlaire.emotion.tools.EmotionSelectorD;

public class Registration {
    public static final String EMOTION_SELECTOR_ID = "emotion_selector";
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "emotion");
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "emotion");

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
    // Some common properties for our blocks and items
    public static final RegistryObject<Block> FIRST_BLOCK = BLOCKS.register("first_block", () -> new Block(defaultBlockProperties()));
    public static final RegistryObject<Item> FIRST_BLOCK_ITEM = formBlock("first_block", FIRST_BLOCK);
    public static final RegistryObject<Item> EMOTION_SELECTOR_ITEM = ITEMS.register(EMOTION_SELECTOR_ID,
            () -> new Item(new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<EmotionSelectorA> EMOTION_SELECTOR_A = ITEMS.register("emotion_a",
            () -> new EmotionSelectorA(defaultItemProperties()));
    public static final RegistryObject<EmotionSelectorB> EMOTION_SELECTOR_B = ITEMS.register("emotion_b",
            () -> new EmotionSelectorB(defaultItemProperties()));
    public static final RegistryObject<EmotionSelectorC> EMOTION_SELECTOR_C = ITEMS.register("emotion_c",
            () -> new EmotionSelectorC(defaultItemProperties()));
    public static final RegistryObject<EmotionSelectorD> EMOTION_SELECTOR_D = ITEMS.register("emotion_d",
            () -> new EmotionSelectorD(defaultItemProperties()));
    private static BlockBehaviour.Properties defaultBlockProperties() {
        return BlockBehaviour.Properties.of(Material.STONE).strength(2f).requiresCorrectToolForDrops();
    }
    private static Item.Properties defaultItemProperties() {
        return new Item.Properties().tab(ModSetup.ITEM_GROUP);
    }
    private static RegistryObject<Item> formBlock(String name, RegistryObject<Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), defaultItemProperties()));
    }


}