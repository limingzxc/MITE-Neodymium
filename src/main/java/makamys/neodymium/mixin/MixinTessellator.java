package makamys.neodymium.mixin;

import makamys.neodymium.config.NeodymiumConfig;
import net.minecraft.TessellatorMITE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import makamys.neodymium.ducks.NeodymiumTessellator;
import makamys.neodymium.renderer.ChunkMesh;
import net.minecraft.Tessellator;

@Mixin(Tessellator.class)
public abstract class MixinTessellator implements NeodymiumTessellator {
    @Shadow public abstract void reset();

    @Shadow public boolean isDrawing;
    // ---additions---
    @Unique
    private ChunkMesh nd$captureTarget;

    @Override
    public boolean nd$captureData() {
        if(nd$captureTarget != null) {
            nd$captureTarget.addTessellatorData((Tessellator)(Object)this);

            if(NeodymiumConfig.enabled.getBooleanValue() && !NeodymiumConfig.enableVanillaChunkMeshes.getBooleanValue()) {
                isDrawing = false;
                reset();
                return true;
            }
        }
        return false;
    }

    @Override
    public ChunkMesh nd$getCaptureTarget() {
        return nd$captureTarget;
    }

    @Override
    public void nd$setCaptureTarget(ChunkMesh target) {
        nd$captureTarget = target;
    }

    // ---mixins---

    @Inject(method = "draw",
            at = @At(value = "HEAD"),
            cancellable = true,
            require = 1)
    private void preDraw(CallbackInfoReturnable<Integer> cir) {
        if (nd$captureData()) {
            cir.setReturnValue(0);
        }
    }
}
