package zlaire.emotion.tools;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EmotionSelectorA extends Item {
    private String kind="A";
    private String message;
    public EmotionSelectorA(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        if(world.isClientSide()){
            player.sendMessage(new TextComponent(player.getDisplayName().getString()+" choose "+kind), Util.NIL_UUID);
            player.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH,1f,1f);
        }
        return super.use(world, player, hand);
    }

//    public InteractionResult use(UseOnContext pContext) {
//        if(pContext.getServer().getLevel().isClientSide()){
//            Player player= pContext.getPlayer();
//            String command="playsound minecraft:entity.firework_rocket.launch player @p";
//            if (player != null) {
//                player.sendMessage(new TextComponent(player.getDisplayName().getString()+" choose "+kind), Util.NIL_UUID);
//                player.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH,1f,1f);
//            }
//        }
//
//        return super.useOn(pContext);
//    }
}
