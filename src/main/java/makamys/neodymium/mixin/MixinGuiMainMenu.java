package makamys.neodymium.mixin;

import makamys.neodymium.Neodymium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.GuiMainMenu;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu {
    @Shadow
    private String splashText;
    
    @Inject(method = "<init>",
            at = @At("RETURN"),
            require = 1)
    private void postConstructor(CallbackInfo ci) {
        splashText = Neodymium.modifySplash(splashText);
    }
}
