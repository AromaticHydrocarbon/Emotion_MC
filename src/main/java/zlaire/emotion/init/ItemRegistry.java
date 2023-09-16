package zlaire.emotion.init;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ItemRegistry {

    public static void writeAllItemNamesToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            ForgeRegistries.ITEMS.forEach(item -> {
                try {
                    writer.write(item.getRegistryName() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeAllEntityNamesToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            ForgeRegistries.ENTITIES.forEach(entityType -> {
                ResourceLocation key = ForgeRegistries.ENTITIES.getKey(entityType);
                try {
                    writer.write(key.toString() + "\n");
                }catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
