package makamys.neodymium.mixin;

import net.minecraft.GameSettings;
import net.minecraft.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin {
    @Redirect(method = "loadRenderers", at = @At(value = "INVOKE", target = "Lnet/minecraft/GameSettings;isFancyGraphicsEnabled()Z"))
    public boolean isFancyGraphicsEnabled(GameSettings gameSettings) {
        return true;
    }
}
