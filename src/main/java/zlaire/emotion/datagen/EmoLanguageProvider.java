package zlaire.emotion.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import zlaire.emotion.setup.ModSetup;
import zlaire.emotion.setup.Registration;

import static zlaire.emotion.setup.Registration.*;

public class EmoLanguageProvider extends LanguageProvider {

    public EmoLanguageProvider(DataGenerator gen, String locale) {
        super(gen, "emotion", locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + ModSetup.GROUP_NAME, "Emotion");
        add(Registration.FIRST_BLOCK.get(), "First Block");
        this.add(EMOTION_SELECTOR_ITEM.get(), "Emotion Selector");
        this.add(EMOTION_SELECTOR_A.get(), "Emotion A");
        this.add(EMOTION_SELECTOR_B.get(), "Emotion B");
        this.add(EMOTION_SELECTOR_C.get(), "Emotion C");
        this.add(EMOTION_SELECTOR_D.get(), "Emotion D");
    }
}