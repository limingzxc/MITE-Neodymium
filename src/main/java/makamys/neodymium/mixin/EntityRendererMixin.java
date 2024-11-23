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
    @Unique
    private static boolean openGL14;
    @Unique
    private static boolean field_153211_u;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initMixin(Minecraft par1Minecraft, CallbackInfo ci) {
        ContextCapabilities var0 = GLContext.getCapabilities();
        openGL14 = var0.OpenGL14 || var0.GL_EXT_blend_func_separate;
        field_153211_u = var0.GL_EXT_blend_func_separate && !var0.OpenGL14;
    }

    @Redirect(method = "renderWorld",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColorMask(ZZZZ)V",ordinal = 0),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;ambientOcclusion:I", ordinal = 1)))
    private void glColorMaskMixin(boolean red, boolean green, boolean blue, boolean alpha) {
        GL11.glEnable(GL11.GL_BLEND);
    }

    @Redirect(method = "renderWorld",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/RenderGlobal;sortAndRender(Lnet/minecraft/EntityLivingBase;ID)I", ordinal = 0),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;ambientOcclusion:I", ordinal = 1)))
    private int glColorMaskMixin(RenderGlobal instance, EntityLivingBase var5, int var20, double var26) {

        glBlendFunc(770, 771, 1, 0);
        // 使renderAllRenderLists方法被调用，但是会被blockVanillaChunkRendering方法取消
        // 从而实现与着色器mixin兼容的骚操作
        return 1;
    }

    @Inject(method = "renderWorld",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColorMask(ZZZZ)V", ordinal = 3),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;ambientOcclusion:I", ordinal = 1)))
    private void glColorMaskMixin2(float par1, long par2, CallbackInfo ci) {}

    @Inject(method = "renderWorld",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glShadeModel(I)V", ordinal = 1),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;ambientOcclusion:I", ordinal = 1)))
    private void glShadeModelMixin(float par1, long par2, CallbackInfo ci) {
        this.mc.renderGlobal.sortAndRender(this.mc.renderViewEntity, 1, par1);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Unique
    private static void glBlendFunc(int p_148821_0_, int p_148821_1_, int p_148821_2_, int p_148821_3_)
    {
        if (openGL14)
        {
            if (field_153211_u)
            {
                EXTBlendFuncSeparate.glBlendFuncSeparateEXT(p_148821_0_, p_148821_1_, p_148821_2_, p_148821_3_);
            }
            else
            {
                GL14.glBlendFuncSeparate(p_148821_0_, p_148821_1_, p_148821_2_, p_148821_3_);
            }
        }
        else
        {
            GL11.glBlendFunc(p_148821_0_, p_148821_1_);
        }
    }

//    @Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/GameSettings;isFancyGraphicsEnabled()Z"))
//    public boolean isFancyGraphicsEnabled(GameSettings gameSettings) {
//        return false;
//    }

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
        if (this.mc.renderViewEntity instanceof EntityPlayer player) {
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