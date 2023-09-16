package zlaire.emotion.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class EmoItemTags extends ItemTagsProvider {

    public EmoItemTags(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(generator, blockTags, "emotion", helper);
    }

    @Override
    protected void addTags() {

    }

    @Override
    public String getName() {
        return "Emotion Tags";
    }
}