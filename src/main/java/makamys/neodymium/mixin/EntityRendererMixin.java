package makamys.neodymium.mixin;

import makamys.neodymium.Neodymium;
import net.minecraft.*;
import org.lwjgl.opengl.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Shadow private Minecraft mc;
    @Shadow private Entity pointedEntity;

    @Inject(method = "setupFog", at = @At("HEAD"))
    private void setupFogMixin(int par1, float par2, CallbackInfo ci) {
        Neodymium.instance.onRenderFog();
    }

    /**
     * @author limingzxc
     * @reason Fix memory leaks
     */
    @Overwrite
    public void getMouseOver(float partial_tick) {
        if (this.mc.renderViewEntity == null || this.mc.theWorld == null) {
            return;
        }
        if (this.mc.renderViewEntity instanceof EntityPlayer player && this.mc.theWorld != null) {
            this.mc.objectMouseOver = player.getSelectedObject(partial_tick, false);
            this.pointedEntity = null;
            this.mc.pointedEntityLiving = null;
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.isEntity()) {
                this.pointedEntity = this.mc.objectMouseOver.getEntityHit();
                if (this.pointedEntity instanceof EntityLivingBase) {
                    this.mc.pointedEntityLiving = (EntityLivingBase) this.pointedEntity;
                }
            }
            if (Minecraft.inDevMode()) {
                EntityRenderer.setDebugInfoForSelectedObject(player.getSelectedObject(partial_tick, false, true, null), player);
            }
        } else {
            Minecraft.setErrorMessage("getMouseOver: cannot handle non EntityPlayer entities");
            this.mc.objectMouseOver = null;
            this.pointedEntity = null;
            this.mc.pointedEntityLiving = null;
        }
    }
}