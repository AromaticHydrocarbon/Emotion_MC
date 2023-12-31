package zlaire.emotion.setup;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {
    public static final String GROUP_NAME = "emotion";

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(GROUP_NAME) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.EMOTION_SELECTOR_ITEM.get());
        }
    };
    public static void init(final FMLCommonSetupEvent event) {
    }
}