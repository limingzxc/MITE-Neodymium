package makamys.neodymium.mixin;

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
abstract class MixinRenderGlobal { 
    
    @Shadow
    private WorldRenderer[] sortedWorldRenderers;
    
    @Unique
    private boolean nd$isInsideUpdateRenderers;
    
    @Inject(method = "renderAllRenderLists", at = @At(value = "HEAD"), cancellable = true, require = 1)
    private void blockVanillaChunkRendering(int p1, double p2, CallbackInfo ci) {
        if(!Neodymium.shouldRenderVanillaWorld()) {
            ci.cancel();
        }
    }
    
    @Inject(method = "renderSortedRenderers", at = @At(value = "HEAD"), require = 1)
    public void preRenderSortedRenderers(int startRenderer, int numRenderers, int renderPass, double partialTickTime, CallbackInfoReturnable<Integer> cir) {
        if(Neodymium.isActive()) {
            Neodymium.renderer.preRenderSortedRenderers(renderPass, partialTickTime, sortedWorldRenderers);
        }
    }

//    @Inject(method = "sortAndRender", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;sort([Ljava/lang/Object;Ljava/util/Comparator;)V", shift = At.Shift.AFTER))
//    public void preRenderSortedRenderers2(EntityLivingBase par1EntityLivingBase, int par2, double par3, CallbackInfoReturnable<Integer> cir) {
//        for (int var23 = 0; var23 < 27; ++var23)
//        {
//            ((IWorldRenderer)this.sortedWorldRenderers[var23]).updateRendererSort();
//        }
//    }

    
    @Inject(method = "loadRenderers", at = @At(value = "HEAD"), require = 1)
    public void preLoadRenderers(CallbackInfo ci) {
        Neodymium.destroyRenderer();
    }
    
    @Inject(method = "updateRenderers", at = @At(value = "RETURN"), require = 1)
    public void speedUpChunkUpdatesForDebug(EntityLivingBase par1EntityLivingBase, boolean par2, CallbackInfoReturnable<Boolean> cir) {
        if(Neodymium.isActive() && !nd$isInsideUpdateRenderers) {
            nd$isInsideUpdateRenderers = true;
            for(int i = 0; i < Neodymium.renderer.rendererSpeedup; i++) {
                ((RenderGlobal)(Object)this).updateRenderers(par1EntityLivingBase, par2);
            }
            nd$isInsideUpdateRenderers = false;
        }
    }
}
