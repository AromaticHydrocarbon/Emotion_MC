package zlaire.emotion.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import zlaire.emotion.setup.ModSetup;
import zlaire.emotion.setup.Registration;

import static zlaire.emotion.setup.Registration.*;

public class EmoLanguageZhProvider extends LanguageProvider {

    public EmoLanguageZhProvider(DataGenerator gen, String locale) {
        super(gen, "emotion", locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + ModSetup.GROUP_NAME, "情绪");
        add(Registration.FIRST_BLOCK.get(), "第一个方块");
        this.add(EMOTION_SELECTOR_ITEM.get(), "情绪选择器");
        this.add(EMOTION_SELECTOR_A.get(), "情绪A");
        this.add(EMOTION_SELECTOR_B.get(), "情绪B");
        this.add(EMOTION_SELECTOR_C.get(), "情绪C");
        this.add(EMOTION_SELECTOR_D.get(), "情绪D");
    }
}