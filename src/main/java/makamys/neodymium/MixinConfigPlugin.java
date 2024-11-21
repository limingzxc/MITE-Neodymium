package makamys.neodymium;

import makamys.neodymium.Compat;
import makamys.neodymium.config.NeodymiumConfig;
import net.xiaoyu233.fml.FishModLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.MixinEnvironment.Phase;
import org.spongepowered.asm.mixin.MixinEnvironment.Side;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MixinConfigPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
        if(MixinEnvironment.getCurrentEnvironment().getSide() == Side.SERVER) return;

        Phase phase = MixinEnvironment.getCurrentEnvironment().getPhase();
        if(phase == Phase.INIT) {
            Compat.forceEnableOptiFineDetectionOfFastCraft();
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        if(MixinEnvironment.getCurrentEnvironment().getSide() == Side.SERVER) return Collections.emptyList();

        List<String> mixins = new ArrayList<>();
        Phase phase = MixinEnvironment.getCurrentEnvironment().getPhase();
        if(phase == Phase.DEFAULT) {
            mixins.add("EntityRendererMixin");
            mixins.add("MinecraftMixin");
            mixins.add("MixinRenderGlobal");
            mixins.add("MixinWorldRenderer");
            mixins.add("ServerCommandManagerMixin");

            if (FishModLoader.hasMod("shader-loader")) {
                System.out.println("Detected Shader Loader");
                mixins.add("MixinTessellator");
            } else {
                mixins.add("TessellatorMITEMixin");
            }

            if(NeodymiumConfig.replaceOpenGLSplash.getBooleanValue()) {
                mixins.add("MixinGuiMainMenu");
            }
        }

        return mixins;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }


}
