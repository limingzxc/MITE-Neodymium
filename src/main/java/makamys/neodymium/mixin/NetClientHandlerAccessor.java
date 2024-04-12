package makamys.neodymium.mixin;

import net.minecraft.NetClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetClientHandler.class)
public interface NetClientHandlerAccessor {
    @Accessor
    boolean isDoneLoadingTerrain();
}
