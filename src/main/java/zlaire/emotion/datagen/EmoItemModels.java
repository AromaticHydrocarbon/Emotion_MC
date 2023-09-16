package zlaire.emotion.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import zlaire.emotion.setup.Registration;

import static zlaire.emotion.setup.Registration.EMOTION_SELECTOR_ID;

public class EmoItemModels extends ItemModelProvider {

    public EmoItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, "emotion", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Registration.FIRST_BLOCK.get().getRegistryName().getPath(), modLoc("block/first_block"));
        this.singleTexture(EMOTION_SELECTOR_ID, new ResourceLocation("item/generated"), "layer0", new ResourceLocation("emotion", "item/" + EMOTION_SELECTOR_ID));
        this.singleTexture("emotion_a", new ResourceLocation("item/generated"), "layer0", new ResourceLocation("emotion", "item/" + "emotion_a"));
        this.singleTexture("emotion_b", new ResourceLocation("item/generated"), "layer0", new ResourceLocation("emotion", "item/" + "emotion_b"));
        this.singleTexture("emotion_c", new ResourceLocation("item/generated"), "layer0", new ResourceLocation("emotion", "item/" + "emotion_c"));
        this.singleTexture("emotion_d", new ResourceLocation("item/generated"), "layer0", new ResourceLocation("emotion", "item/" + "emotion_d"));
    }
}