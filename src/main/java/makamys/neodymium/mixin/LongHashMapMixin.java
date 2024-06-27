package makamys.neodymium.mixin;

import net.minecraft.LongHashMap;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LongHashMap.class)
public abstract class LongHashMapMixin {

    /**
     * @author embeddedt
     * @reason Use a better hash (from TMCW) that avoids collisions.
     */
    @Overwrite
    public static int getHashedKey(long par0)
    {
        return (int)par0 + (int)(par0 >>> 32) * 92821;
    }
}