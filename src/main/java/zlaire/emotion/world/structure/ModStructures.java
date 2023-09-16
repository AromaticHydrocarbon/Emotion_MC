package zlaire.emotion.world.structure;

import zlaire.emotion.Emotion;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraftforge.versions.forge.ForgeVersion.MOD_ID;

public class ModStructures {
    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MOD_ID);

    //我们刚刚的建筑类
    public static final RegistryObject<StructureFeature<?>> Cave_STRUCTURE =
            DEFERRED_REGISTRY_STRUCTURE.register("church_structure", CaveStructure::new);

    //public static final RegistryObject<StructureFeature<?>> Tank_STRUCTURE =
    //        DEFERRED_REGISTRY_STRUCTURE.register("tank_structure", TankStructure::new);

    public static void register(IEventBus eventBus) {
        DEFERRED_REGISTRY_STRUCTURE.register(eventBus);
    }
}
