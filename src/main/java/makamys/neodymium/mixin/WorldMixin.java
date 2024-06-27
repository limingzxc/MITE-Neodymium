package makamys.neodymium.mixin;

import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
    @Unique
    private long key;

    @Inject(method = "getBlockId", at = @At("HEAD"))
    public void getKey(int par1, int par2, int par3, CallbackInfoReturnable<Integer> cir) {
        key = (long)(par1 >> 4) & 0xFFFFFFFFL | ((long)(par3 >> 4) & 0xFFFFFFFFL) << 32;
    }

    @ModifyVariable(method = "getBlockId", at = @At("STORE"), name = "var3_1")
    public int injected(int var3_1) {
        return getHashedKey(key);
    }

    @Unique
    private static int getHashedKey(long par0)
    {
        return (int)par0 + (int)(par0 >>> 32) * 92821;
    }
}
