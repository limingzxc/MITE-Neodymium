package makamys.neodymium.mixin;

import net.minecraft.NetClientHandler;
import net.minecraft.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerControllerMP.class)
public interface PlayerControllerMPAccessor {
    @Accessor
    NetClientHandler getNetClientHandler();
}
