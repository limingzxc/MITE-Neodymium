package makamys.neodymium;

import static makamys.neodymium.Constants.LOGGER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fi.dy.masa.malilib.gui.screen.ModsScreen;
import makamys.neodymium.config.NeodymiumConfig;
import makamys.neodymium.renderer.compat.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.apache.commons.lang3.tuple.Pair;

import makamys.neodymium.Compat.Warning;
import makamys.neodymium.command.NeodymiumCommand;
import makamys.neodymium.renderer.NeoRenderer;
import makamys.neodymium.util.ChatUtil;
import makamys.neodymium.util.WarningHelper;
import net.minecraft.Minecraft;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

public class Neodymium implements ModInitializer, PreLaunchEntrypoint {
    public static Neodymium instance = new Neodymium();
    public static final NeodymiumConfig.ReloadInfo CONFIG_RELOAD_INFO = new NeodymiumConfig.ReloadInfo();
    private static Map<String, Object> properties;
    private boolean renderDebugText = false;
    
    public static NeoRenderer renderer;

    public static RenderUtil util;
    
    private static World rendererWorld;

//    public void construct(FMLConstructionEvent event) {
//        Compat.init();
//    }
    
//    public void init(FMLInitializationEvent event) {
//        FMLCommonHandler.instance().bus().register(this);
//        MinecraftForge.EVENT_BUS.register(this);
//        NeodymiumCommand.init();
//    }
    
//    @SubscribeEvent
//    public void onConfigChanged(ConfigChangedEvent event) {
//        if(event.modID.equals(MODID)) {
//            Config.flush();
//        }
//    }
    
    private void onPlayerWorldChanged(World newWorld) {
//    	if(getRendererWorld() == null && newWorld != null) {
//    		Config.reloadConfig();
//    	}
    	if(renderer != null) {
            destroyRenderer();
        }
    	if(NeodymiumConfig.enabled.getBooleanValue() && newWorld != null) {
    	    Pair<List<Warning>, List<Warning>> warnsAndCriticalWarns = showCompatStatus(false);
    	    List<Warning> warns = warnsAndCriticalWarns.getLeft();
    	    List<Warning> criticalWarns = warnsAndCriticalWarns.getRight();
    	    
    	    if(criticalWarns.isEmpty()) {
                Compat.updateOptiFineShadersState();
                updateRenderUtil();
                renderer = new NeoRenderer(newWorld);
    	        renderer.hasIncompatibilities = !warns.isEmpty() || !criticalWarns.isEmpty();
    	    }
        }
    	rendererWorld = newWorld;
    }
    
//    @SubscribeEvent
//    @SideOnly(Side.CLIENT)
//    public void onWorldUnload(WorldEvent.Unload event) {
//        if(!Config.enabled) return;
//
//        if(event.world == getRendererWorld()) {
//        	onPlayerWorldChanged(null);
//        }
//    }

    public void onWorldUnload(World world) {
        if(!NeodymiumConfig.enabled.getBooleanValue()) return;

        if(world == getRendererWorld()) {
            onPlayerWorldChanged(null);
        }
    }
    
//    @SubscribeEvent
//    @SideOnly(Side.CLIENT)
//    public void onConnectToServer(ClientConnectedToServerEvent event) {
//        Config.reloadConfig();
//        ChatUtil.resetShownChatMessages();
//        WarningHelper.reset();
//    }
    public void onConnectToServer() {
        NeodymiumConfig.getInstance().load();
        ChatUtil.resetShownChatMessages();
//        Compat.reset();
        WarningHelper.reset();
    }
    
    public static boolean isActive() {
        return renderer != null && renderer.hasInited && !renderer.destroyPending;
    }
    
    private World getRendererWorld() {
    	return rendererWorld;
    }
    
//    @SubscribeEvent
//    public void onClientTick(TickEvent.ClientTickEvent event) {
//        if(!Config.enabled) return;
//
//    	if(event.phase == TickEvent.Phase.START && isActive()) {
//    	    if(Config.hotswap) {
//    	        if(Config.reloadIfChanged(CONFIG_RELOAD_INFO)) {
//    	            if(CONFIG_RELOAD_INFO.needReload) {
//    	                Minecraft.getMinecraft().renderGlobal.loadRenderers();
//    	            } else if(renderer != null) {
//    	                renderer.reloadShader();
//    	            }
//    	        }
//    	    }
//    	}
//
//    	if(event.phase == TickEvent.Phase.START) {
//    	    if(Compat.hasChanged()) {
//    	        Pair<List<Warning>, List<Warning>> warns = showCompatStatus(false);
//    	        if(renderer != null) {
//    	            renderer.hasIncompatibilities = !warns.getLeft().isEmpty() || !warns.getRight().isEmpty();
//    	        }
//    	    }
//    	}
//    }

    public void onClientTick() {
        if(!NeodymiumConfig.enabled.getBooleanValue()) return;
        if(isActive()) {
            if(NeodymiumConfig.reloadIfChanged(CONFIG_RELOAD_INFO)) {
                if(CONFIG_RELOAD_INFO.needReload) {
                    Minecraft.getMinecraft().renderGlobal.loadRenderers();
                    CONFIG_RELOAD_INFO.needReload = false;
                } else if(renderer != null) {
                    renderer.reloadShader();
                }
            }
            /*if (Minecraft.getMinecraft().isSingleplayer() && false) {
                ((ServerConfigurationManagerAccessor)Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager()).setViewDistance(
                        Minecraft.getMinecraft().gameSettings.renderDistance < 2 ? 40 >> Minecraft.getMinecraft().gameSettings.renderDistance : 10
                );
            }*/
        }
        if(Compat.hasChanged()) {
            Pair<List<Warning>, List<Warning>> warns = showCompatStatus(false);
            if(renderer != null) {
                renderer.hasIncompatibilities = !warns.getLeft().isEmpty() || !warns.getRight().isEmpty();
            }
        }
    }
    
//    @SubscribeEvent
//    public void onRenderTick(TickEvent.RenderTickEvent event) {
//        if(!Config.enabled) return;
//
//        if(event.phase == TickEvent.Phase.START) {
//            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//            World world = player != null ? player.worldObj : null;
//            if(world != getRendererWorld()) {
//                onPlayerWorldChanged(world);
//            }
//
//            if(isActive()) {
//                renderer.forceRenderFog = true;
//            }
//        } else if(event.phase == TickEvent.Phase.END) {
//            if(renderer != null) {
//                renderer.onRenderTickEnd();
//            }
//        }
//    }

    public void onRenderTick(Phase phase) {
        if(!NeodymiumConfig.enabled.getBooleanValue())
            return;

        if(phase == Phase.START) {
            // Initialization is too slow
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            World world = player != null ? player.worldObj : null;
//            World world = Minecraft.getMinecraft().theWorld;
            if(world != getRendererWorld()) {
                onPlayerWorldChanged(world);
            }

            if(isActive()) {
                renderer.forceRenderFog = true;
            }
        } else if(phase == Phase.END) {
            if(renderer != null) {
                renderer.onRenderTickEnd();
            }
        }
    }
    
//    @SubscribeEvent
//    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
//        if (Config.showDebugInfo && isActive()) {
//            if (event.type.equals(RenderGameOverlayEvent.ElementType.DEBUG)) {
//                renderDebugText = true;
//            } else if (renderDebugText && (event instanceof RenderGameOverlayEvent.Text) && event.type.equals(RenderGameOverlayEvent.ElementType.TEXT)) {
//                renderDebugText = false;
//                RenderGameOverlayEvent.Text text = (RenderGameOverlayEvent.Text) event;
//                text.right.add(null);
//                text.right.addAll(renderer.getDebugText(false));
//            }
//        }
//    }

    public String onRenderOverlay(ElementType type, String text) {
        if (NeodymiumConfig.showDebugInfo.getBooleanValue() && isActive()) {
            if (type.equals(ElementType.DEBUG)) {
                renderDebugText = true;
            } else if (renderDebugText && type.equals(ElementType.TEXT)) {
                renderDebugText = false;
                text += Arrays.toString(renderer.getDebugText(false).toArray());
            }
        }
        return text;
    }
    
//    @SubscribeEvent
//    public void onRenderFog(EntityViewRenderEvent.RenderFogEvent event) {
//        if(isActive()) {
//            renderer.forceRenderFog = false;
//        }
//    }

    public void onRenderFog() {
        if(isActive()) {
            renderer.forceRenderFog = false;
        }
    }

    public static boolean shouldRenderVanillaWorld() {
        return !isActive() || (isActive() && renderer.renderWorld && !renderer.rendererActive);
    }

    public static String modifySplash(String splash) {
        if(splash.equals("OpenGL 1.2!")) {
            return "OpenGL 3.3 (if supported)!";
        } else {
            return splash;
        }
    }

    public static void destroyRenderer() {
        if(renderer != null) {
            renderer.destroy();
            renderer = null;
        }
        rendererWorld = null;
    }
    
    public static Pair<List<Warning>, List<Warning>> showCompatStatus(boolean statusCommand) {
        List<Warning> warns = new ArrayList<>();
        List<Warning> criticalWarns = new ArrayList<>();
        
        Compat.getCompatibilityWarnings(warns, criticalWarns, statusCommand);
        
        if(!criticalWarns.isEmpty() && !NeodymiumConfig.ignoreIncompatibilities.getBooleanValue()) {
            criticalWarns.add(new Warning("Neodymium has been disabled due to a critical incompatibility."));
        }
        
        for(Warning warn : warns) {
            LOGGER.warn(warn.text);
        }
        for(Warning criticalWarn : criticalWarns) {
            LOGGER.warn("Critical: " + criticalWarn.text);
        }
        
        return Pair.of(warns, criticalWarns);
    }

    private static void updateRenderUtil() {
        final boolean hasRPLE = Compat.isRPLEModPresent();
        final boolean hasShaders = Compat.isOptiFineShadersEnabled();

        if (hasShaders) {
            if (hasRPLE) {
                util = RenderUtilShaderRPLE.INSTANCE;
            } else {
                util = RenderUtilShaders.INSTANCE;
            }
        } else {
            if (hasRPLE) {
                util = RenderUtilRPLE.INSTANCE;
            } else {
                util = RenderUtilVanilla.INSTANCE;
            }
        }
    }

    public static Map<String, Object> getProperties() {
        return properties;
    }

    protected static void setProperties(Map<String, Object> propertiesA) {
        if (properties != null && properties != propertiesA) {
            throw new RuntimeException("Duplicate setProperties call!");
        }

        properties = propertiesA;
    }

    @Override
    public void onInitialize() {
        NeodymiumCommand.init();
        NeodymiumConfig.init();
        NeodymiumConfig.getInstance().load();
        ModsScreen.getInstance().addConfig(NeodymiumConfig.getInstance());
    }

    @Override
    public void onPreLaunch() {

    }
}
