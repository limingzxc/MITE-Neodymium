package makamys.neodymium.mixin;

import makamys.neodymium.Compat;
import makamys.neodymium.Neodymium;
import net.minecraft.EntityLivingBase;
import net.minecraft.RenderGlobal;
import net.minecraft.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Blocks vanilla chunk rendering while NeoRenderer is active. */
@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
    @Unique
    private boolean nd$isInsideUpdateRenderers;

    @Shadow
    public WorldRenderer[] sortedWorldRenderers;

    @Inject(method = "renderAllRenderLists",
            at = @At(value = "HEAD"),
            cancellable = true,
            require = 1)
    private void blockVanillaChunkRendering(int p1, double p2, CallbackInfo ci) {
        if(!Neodymium.shouldRenderVanillaWorld()) {
            ci.cancel();
        }
    }
    
    @Inject(method = "renderSortedRenderers",
            at = @At(value = "HEAD"),
            cancellable = true,
            require = 1)
    public void preRenderSortedRenderers(int startRenderer, int numRenderers, int renderPass, double partialTickTime, CallbackInfoReturnable<Integer> cir) {
        if(Neodymium.isActive()) {
            int updated = Neodymium.renderer.preRenderSortedRenderers(renderPass, partialTickTime, sortedWorldRenderers);
            if (!Compat.keepRenderListLogic()) {
                cir.setReturnValue(updated);
            }
        }
    }
    
    @Inject(method = "loadRenderers",
            at = @At(value = "HEAD"),
            require = 1)
    public void preLoadRenderers(CallbackInfo ci) {
//        if (Compat.isOptiFineShadersEnabled()) {
            Neodymium.destroyRenderer();
//        }
    }
    
    @Inject(method = "updateRenderers",
            at = @At(value = "RETURN"),
            require = 1)
    public void speedUpChunkUpdatesForDebug(EntityLivingBase entity, boolean flag, CallbackInfoReturnable<Boolean> cir) {
        if(Neodymium.isActive() && !nd$isInsideUpdateRenderers) {
            nd$isInsideUpdateRenderers = true;
            for(int i = 0; i < Neodymium.renderer.rendererSpeedup; i++) {
                ((RenderGlobal)(Object)this).updateRenderers(entity, flag);
            }
            nd$isInsideUpdateRenderers = false;
        }
    }
}
