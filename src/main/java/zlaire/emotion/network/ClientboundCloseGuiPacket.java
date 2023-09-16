package zlaire.emotion.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import zlaire.emotion.screen.CloseGuI;
import zlaire.emotion.screen.OpenGuI;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ClientboundCloseGuiPacket {
    public ClientboundCloseGuiPacket() {
    }

    public ClientboundCloseGuiPacket(FriendlyByteBuf buffer) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> CloseGuI::CloseGUI);
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
