package zlaire.emotion.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import zlaire.emotion.setup.Registration;

public class EmoBlockTags extends BlockTagsProvider {

    public EmoBlockTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, "emotion", helper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.FIRST_BLOCK.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(Registration.FIRST_BLOCK.get());
    }

    @Override
    public String getName() {
        return "Emotion Tags";
    }
}