package notjoe.stockpile.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import notjoe.stockpile.blockentity.GraveBlockEntity$;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world_1, GameProfile gameProfile_1) {
        super(world_1, gameProfile_1);
    }

    @Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V", at = @At("HEAD"))
    public void onDeathHead(DamageSource damageSource, CallbackInfo ci) {
        if (world.getGameRules().getBoolean("keepInventory")) {
            return;
        }

        GraveBlockEntity$.MODULE$.createGrave(world, inventory, getPos());
        inventory.clear();
    }

    @Shadow
    public abstract boolean isSpectator();

    @Shadow
    public abstract boolean isCreative();
}
