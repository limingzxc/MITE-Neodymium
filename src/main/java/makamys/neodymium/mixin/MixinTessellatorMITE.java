package makamys.neodymium.mixin;

import makamys.neodymium.config.Config;
import makamys.neodymium.ducks.ITessellator;
import makamys.neodymium.renderer.ChunkMesh;
import net.minecraft.Tessellator;
import net.minecraft.TessellatorMITE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TessellatorMITE.class)
abstract class MixinTessellatorMITE extends Tessellator implements ITessellator {
    
    @Unique
    private boolean nd$captureMeshes;

    @Inject(method = "draw", at = @At(value = "HEAD"), cancellable = true)
    private void preDraw(CallbackInfoReturnable<Integer> cir) {
        if(nd$captureMeshes) {
            ChunkMesh.preTessellatorDraw((Tessellator)(Object)this);
            
            if(Config.enabled && !Config.enableVanillaChunkMeshes) {
                isDrawing = false;
                reset();
                cir.setReturnValue(0);
            }
        }
    }
    
    @Override
    public void neodymium$enableMeshCapturing(boolean enable) {
        nd$captureMeshes = enable;
    }
    
}
