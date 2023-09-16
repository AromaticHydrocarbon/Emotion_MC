package zlaire.emotion.init;

import net.minecraft.resources.ResourceLocation;


import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import zlaire.emotion.network.ClientboundCloseGuiPacket;
import zlaire.emotion.network.ClientboundShowEmotionScreenPacket;


public final class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("emotion", "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private PacketHandler() {
    }

    public static void init() {
        int index = 0;
        INSTANCE.messageBuilder(ClientboundShowEmotionScreenPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientboundShowEmotionScreenPacket::encode).decoder(ClientboundShowEmotionScreenPacket::new)
                .consumer(ClientboundShowEmotionScreenPacket::handle).add();
//        INSTANCE.messageBuilder(ClientboundCloseGuiPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
//                .encoder(ClientboundCloseGuiPacket::encode).decoder(ClientboundCloseGuiPacket::new)
//                .consumer(ClientboundCloseGuiPacket::handle).add();
    }
}
