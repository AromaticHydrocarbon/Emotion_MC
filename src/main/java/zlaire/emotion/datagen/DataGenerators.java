package zlaire.emotion.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = "emotion", bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        var helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(new EmoRecipes(generator));
            generator.addProvider(new EmoLootTables(generator));
            EmoBlockTags blockTags = new EmoBlockTags(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);
            generator.addProvider(new EmoItemTags(generator, blockTags, event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
            generator.addProvider(new EmoBlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new EmoItemModels(generator, event.getExistingFileHelper()));
            generator.addProvider(new EmoLanguageProvider(generator, "en_us"));
        }
    }
}