package zlaire.emotion.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import zlaire.emotion.setup.Registration;

public class EmoBlockStates extends BlockStateProvider {

    public EmoBlockStates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, "emotion", helper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.FIRST_BLOCK.get());
    }
}