package makamys.neodymium.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.util.JsonUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class NeodymiumConfig extends SimpleConfigs {
    public static NeodymiumConfig instance;

    public static List<ConfigBase> values = new ArrayList<>();
    public static List<ConfigHotkey> hotkeys = new ArrayList<>();
    public static List<ConfigBase> _general;
    public static List<ConfigBase> render;
    public static List<ConfigBase> misc;
    public static List<ConfigBase> debug;

    // @ConfigBoolean(cat="_general", def=true, com="Set this to false to fully disable the mod.")
    public static ConfigBoolean enabled = new ConfigBoolean("enabledNeodymium", true, "enabledNeodymium");

    // @ConfigBoolean(cat="_general", def=false, com="Apply changes made in the config file immediately without having to manually reload the renderer. Off by default because it could potentially cause poor performance on certain platforms.")
    // public static ConfigBoolean hotswap = new ConfigBoolean("热加载", false, "立即应用在配置文件中所做的更改，而无需手动重新加载渲染器。默认情况下处于关闭状态，因为它可能会导致某些平台上的性能不佳。");

    // @NeedsReload
    // @ConfigBoolean(cat="render", def=false, com="Simplify chunk meshes so they are made of less vertices. Reduces vertex count at the cost of increasing shader complexity. It seems to reduce performance overall.")
    public static ConfigBooleanChangeCallback simplifyChunkMeshes = new ConfigBooleanChangeCallback("simplifyChunkMeshes", false, "simplifyChunkMeshes");

    // @ConfigBoolean(cat="render", def=true, com="Don't submit faces for rendering if they are facing away from the camera. Reduces GPU workload at the cost of increasing driver overhead. This will improve the framerate most of the time, but may reduce it if you are not fillrate-limited (such as when playing on a small resolution).")
    public static ConfigBoolean cullFaces = new ConfigBoolean("cullFaces", true, "cullFaces");

    // @NeedsReload
    // @ConfigBoolean(cat="render", def=false, com="Store texture coordinates as shorts instead of floats. Slightly reduces memory usage and might improve performance by small amount. Might affect visuals slightly, but it's only noticable if the texture atlas is huge.")
    public static ConfigBooleanChangeCallback shortUV = new ConfigBooleanChangeCallback("shortUV", false, "shortUV");

    // @ConfigInt(cat="render", def=256,min=16,max=1024,com = "The size of the allocation chunks for opaque geometry (in Megabytes). Requires game restart to apply.")
    public static ConfigInteger bufferSizePass0 = new ConfigInteger("bufferSizePass0", 256, 16, 1024, "bufferSizePass0");

    // @ConfigInt(cat="render", def=64,min=16,max=1024,com = "The size of the allocation chunks for transparent geometry (in Megabytes). Requires game restart to apply.")
    public static ConfigInteger bufferSizePass1 = new ConfigInteger("bufferSizePass1", 64, 16, 1024, "bufferSizePass1");

    // @ConfigInt(cat="render", def=1, min=1, max=Integer.MAX_VALUE, com="Interval (in frames) between the sorting of transparent meshes. Increasing this will reduce CPU usage, but also increase the likelyhood of graphical artifacts appearing when transparent chunks are loaded.")
    public static ConfigInteger sortFrequency = new ConfigInteger("sortFrequency", 1, 1, 200, "sortFrequency");

    // @ConfigBoolean(cat="render", def=true, com="Don't render meshes that are shrouded in fog. OptiFine also does this when fog is turned on, this setting makes Neodymium follow suit.")
    public static ConfigBoolean fogOcclusion = new ConfigBoolean("fogOcclusion", true, "fogOcclusion");

    // @ConfigBoolean(cat="render", def=false, com="Do fog occlusion even if fog is disabled.")
    public static ConfigBoolean fogOcclusionWithoutFog = new ConfigBoolean("fogOcclusionWithoutFog", false, "fogOcclusionWithoutFog");

    // @NeedsReload
    // @ConfigInt(cat="render", def=512, min=1, max=Integer.MAX_VALUE, com="VRAM buffer size (MB). 512 seems to be a good value on Normal render distance. Increase this if you encounter warnings about the VRAM getting full. Does not affect RAM usage.")
//    public static ConfigIntegerChangeCallback VRAMSize = new ConfigIntegerChangeCallback("VRAMSize", 512, 256, 2048, "VRAMSize");

    // @ConfigEnum(cat="render", def="auto", clazz=AutomatableBoolean.class, com="Render fog? Slightly reduces framerate. `auto` means the OpenGL setting will be respected (as set by mods like OptiFine).\nValid values: true, false, auto")
    public static ConfigBoolean renderFog = new ConfigBoolean("renderFog", true, "renderFog");

    // @ConfigInt(cat="render", def=Integer.MAX_VALUE, min=0, max=Integer.MAX_VALUE, com="Chunks further away than this distance (in chunks) will not have unaligned quads such as tall grass rendered.")
    public static ConfigInteger maxUnalignedQuadDistance = new ConfigInteger("maxUnalignedQuadDistance", 64, 0, 64, "maxUnalignedQuadDistance");

    // @ConfigBoolean(cat="misc", def=true, com="Replace splash that says 'OpenGL 1.2!' with 'OpenGL 3.3!'. Just for fun.")
    public static ConfigBoolean replaceOpenGLSplash = new ConfigBoolean("replaceOpenGLSplash", true, "replaceOpenGLSplash");

    // @ConfigBoolean(cat="misc", def=false, com="Don't warn about incompatibilities in chat, and activate renderer even in spite of critical ones.")
    public static ConfigBoolean ignoreIncompatibilities = new ConfigBoolean("ignoreIncompatibilities", false, "ignoreIncompatibilities");

    // @ConfigBoolean(cat="misc", def=false, com="Don't print non-critical rendering errors.")
    public static ConfigBoolean silenceErrors = new ConfigBoolean("silenceErrors", false, "silenceErrors");

    // @ConfigInt(cat="debug", def=-1, min=-1, max=Integer.MAX_VALUE)
    public static ConfigInteger maxMeshesPerFrame = new ConfigInteger("maxMeshesPerFrame", -1, -1, 6000, "maxMeshesPerFrame");

    // @ConfigInt(cat="debug", def=Keyboard.KEY_F4, min=-1, max=Integer.MAX_VALUE, com="The LWJGL keycode of the key that has to be held down while pressing the debug keybinds. Setting this to 0 will make the keybinds usable without holding anything else down. Setting this to -1 will disable debug keybinds entirely.")
    public static ConfigHotkey debugPrefix = new ConfigHotkey("debugPrefix", Keyboard.KEY_F4, "debugPrefix");

    // @ConfigBoolean(cat="debug", def=true, com="Set this to false to stop showing the debug info in the F3 overlay.")
    public static ConfigBoolean showDebugInfo = new ConfigBoolean("showDebugInfo", true, "showDebugInfo");

    // @ConfigBoolean(cat="debug", def=false)
    public static ConfigBoolean wireframe = new ConfigBoolean("wireframe", false);

    // @ConfigBoolean(cat="debug", def=false, com="Enable building of vanilla chunk meshes. Makes it possible to switch to the vanilla renderer on the fly, at the cost of reducing chunk update performance.")
    public static ConfigBoolean enableVanillaChunkMeshes = new ConfigBoolean("enableVanillaChunkMeshes", false, "enableVanillaChunkMeshes");
    
//    private static Map<String, Map<String, String>> config;
//    private static File configFile = null;//new File(FabricLauncherBase.getLauncher()., "config/" + MODID + ".cfg");
//    private static WatchService watcher;

    public NeodymiumConfig(String name, List<ConfigHotkey> hotkeys, List<ConfigBase> values) {
        super(name, hotkeys, values);
    }

    public static void init() {
        _general = List.of(enabled);
        render = List.of(simplifyChunkMeshes, cullFaces, shortUV, sortFrequency, fogOcclusion, fogOcclusionWithoutFog,
                bufferSizePass0, bufferSizePass1, renderFog, maxUnalignedQuadDistance);
        misc = List.of(replaceOpenGLSplash, ignoreIncompatibilities, silenceErrors);
        debug = List.of(maxMeshesPerFrame, showDebugInfo, wireframe, enableVanillaChunkMeshes);

        values.addAll(_general);
        values.addAll(render);
        values.addAll(misc);
        values.addAll(debug);

        hotkeys.add(debugPrefix);

        instance = new NeodymiumConfig("Neodymium", hotkeys, values);
    }

    public static NeodymiumConfig getInstance() {
        return instance;
    }

    @Override
    public void save() {
        JsonObject root = new JsonObject();
        ConfigUtils.writeConfigBase(root, "general", _general);
        ConfigUtils.writeConfigBase(root, "render", render);
        ConfigUtils.writeConfigBase(root, "misc", misc);
        ConfigUtils.writeConfigBase(root, "debug", debug);
        JsonUtils.writeJsonToFile(root, this.getOptionsFile());
    }

    @Override
    public void load() {
        if (!this.getOptionsFile().exists()) {
            this.save();
        } else {
            JsonElement jsonElement = JsonUtils.parseJsonFile(this.getOptionsFile());
            if (jsonElement != null && jsonElement.isJsonObject()) {
                JsonObject root = jsonElement.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "general", _general);
                ConfigUtils.readConfigBase(root, "render", render);
                ConfigUtils.readConfigBase(root, "misc", misc);
                ConfigUtils.readConfigBase(root, "debug", debug);
            }
        }
    }


//    private void registerProperties() {
//        registerProperty("enabled", "true", "Set this to false to fully disable the mod.");
//        registerProperty("hotswap", "false", "DOESNT WORK: Apply changes made in the config file immediately without having to manually reload the renderer. Off by default because it could potentially cause poor performance on certain platforms.");
//        registerProperty("simplifyChunkMeshes", "false", "Simplify chunk meshes so they are made of less vertices. Reduces vertex count at the cost of increasing shader complexity. It seems to reduce performance overall.");
//        registerProperty("cullFaces", "true", "Don't submit faces for rendering if they are facing away from the camera. Reduces GPU workload at the cost of increasing driver overhead. This will improve the framerate most of the time, but may reduce it if you are not fillrate-limited (such as when playing on a small resolution).");
//        registerProperty("shortUV", "false", "Store texture coordinates as shorts instead of floats. Slightly reduces memory usage and might improve performance by small amount. Might affect visuals slightly, but it's only noticable if the texture atlas is huge.");
//        registerProperty("sortFrequency", "1", "Interval (in frames) between the sorting of transparent meshes. Increasing this will reduce CPU usage, but also increase the likelyhood of graphical artifacts appearing when transparent chunks are loaded.");
//        registerProperty("fogOcclusion", "true", "Don't render meshes that are shrouded in fog. OptiFine also does this when fog is turned on, this setting makes Neodymium follow suit.");
//        registerProperty("fogOcclusionWithoutFog", "false", "Do fog occlusion even if fog is disabled.");
//        registerProperty("VRAMSize", "512", "VRAM buffer size (MB). 512 seems to be a good value on Normal render distance. Increase this if you encounter warnings about the VRAM getting full. Does not affect RAM usage.");
//        registerProperty("renderFog", "auto", "Render fog? Slightly reduces framerate. `auto` means the OpenGL setting will be respected (as set by mods like OptiFine).\nValid values: true, false, auto");
//        registerProperty("maxUnalignedQuadDistance", "2147483647", "Chunks further away than this distance (in chunks) will not have unaligned quads such as tall grass rendered.");
//        registerProperty("replaceOpenGLSplash", "true", "DOESNT WORK: Replace splash that says 'OpenGL 1.2!' with 'OpenGL 3.3!'. Just for fun.");
//        registerProperty("ignoreIncompatibilities", "false", "Don't warn about incompatibilities in chat, and activate renderer even in spite of critical ones.");
//        registerProperty("silenceErrors", "false", "Don't print non-critical rendering errors.");
//        registerProperty("maxMeshesPerFrame", "-1", "");
//        registerProperty("debugPrefix", "61", "The LWJGL keycode of the key that has to be held down while pressing the debug keybinds. Setting this to 0 will make the keybinds usable without holding anything else down. Setting this to -1 will disable debug keybinds entirely.");
//        registerProperty("showDebugInfo", "true", "Set this to false to stop showing the debug info in the F3 overlay.");
//        registerProperty("wireframe", "false", "");
//        registerProperty("enableVanillaChunkMeshes", "false", "Enable building of vanilla chunk meshes. Makes it possible to switch to the vanilla renderer on the fly, at the cost of reducing chunk update performance.");
//    }

//    public void handleConfigProperties(Map<String, String> propertyValues) {
//        enabled = Boolean.parseBoolean(propertyValues.get("enabled"));
//        hotswap = Boolean.parseBoolean(propertyValues.get("hotswap"));
//        simplifyChunkMeshes = Boolean.parseBoolean(propertyValues.get("simplifyChunkMeshes"));
//        cullFaces = Boolean.parseBoolean(propertyValues.get("cullFaces"));
//        shortUV = Boolean.parseBoolean(propertyValues.get("shortUV"));
//        sortFrequency = Integer.parseInt(propertyValues.get("sortFrequency"));
//        fogOcclusion = Boolean.parseBoolean(propertyValues.get("fogOcclusion"));
//        fogOcclusionWithoutFog = Boolean.parseBoolean(propertyValues.get("fogOcclusionWithoutFog"));
//        VRAMSize = Integer.parseInt(propertyValues.get("VRAMSize"));
//        renderFog = AutomatableBoolean.valueOf(propertyValues.get("renderFog").toUpperCase());
//        maxUnalignedQuadDistance = Integer.parseInt(propertyValues.get("maxUnalignedQuadDistance"));
//        replaceOpenGLSplash = Boolean.parseBoolean(propertyValues.get("replaceOpenGLSplash"));
//        ignoreIncompatibilities = Boolean.parseBoolean(propertyValues.get("ignoreIncompatibilities"));
//        silenceErrors = Boolean.parseBoolean(propertyValues.get("silenceErrors"));
//        maxMeshesPerFrame = Integer.parseInt(propertyValues.get("maxMeshesPerFrame"));
//        debugPrefix = Integer.parseInt(propertyValues.get("debugPrefix"));
//        showDebugInfo = Boolean.parseBoolean(propertyValues.get("showDebugInfo"));
//        wireframe = Boolean.parseBoolean(propertyValues.get("wireframe"));
//        enableVanillaChunkMeshes = Boolean.parseBoolean(propertyValues.get("enableVanillaChunkMeshes"));
//    }

    /*public static void reloadConfig(ReloadInfo info) {
        try {
            if(configFile.exists() && Files.size(configFile.toPath()) == 0) {
                // Sometimes the watcher fires twice, and the first time the file is empty.
                // I don't know why. This is the workaround.
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        config = new Configuration(configFile, VERSION);
        
        config.load();
        
        boolean needReload = loadFields(config);
        if(info != null) {
            info.needReload = needReload;
        }
        Map map = new HashMap<>();
        map.put("", "Note: Some debug features are only available in creative mode or dev environments.");
        config.put("debug", map);
        
        if(config.hasChanged() || (!config.getLoadedConfigVersion().equals(config.getDefinedConfigVersion()))) {
            config.save();
        }
        
        if(hotswap && watcher == null) {
            try {
                registerWatchService();
            } catch(IOException e) {
                LOGGER.warn("Failed to register watch service: " + e + " (" + e.getMessage() + "). Changes to the config file will not be reflected");
            }
        }
    }*/


    /*private static boolean loadFields(Map<String, Map<String, String>> config) {
        boolean needReload = false;

        for(Field field : NeodymiumConfig.class.getFields()) {
            if(!Modifier.isStatic(field.getModifiers())) continue;

            NeedsReload needsReload = null;
            ConfigBoolean configBoolean = null;
            ConfigInt configInt = null;
            ConfigEnum configEnum = null;

            for(Annotation an : field.getAnnotations()) {
                if(an instanceof NeedsReload) {
                    needsReload = (NeedsReload) an;
                } else if(an instanceof ConfigInt) {
                    configInt = (ConfigInt) an;
                } else if(an instanceof ConfigBoolean) {
                    configBoolean = (ConfigBoolean) an;
                } else if(an instanceof ConfigEnum) {
                    configEnum = (ConfigEnum) an;
                }
            }

            if(configBoolean == null && configInt == null && configEnum == null) continue;

            Object currentValue = null;
            Object newValue = null;
            try {
                currentValue = field.get(null);
            } catch (Exception e) {
                LOGGER.error("Failed to get value of field " + field.getName());
                e.printStackTrace();
                continue;
            }

            if(configBoolean != null) {
                newValue = config.getBoolean(field.getName(), configBoolean.cat(), configBoolean.def(), configBoolean.com());
            } else if(configInt != null) {
                newValue = config.getInt(field.getName(), configInt.cat(), configInt.def(), configInt.min(), configInt.max(), configInt.com());
            } else if(configEnum != null) {
                boolean lowerCase = true;

                Class<? extends Enum> configClass = configEnum.clazz();
                Map<String, ? extends Enum> enumMap = EnumUtils.getEnumMap(configClass);
                String[] valuesStrUpper = (String[])enumMap.keySet().stream().toArray(String[]::new);
                String[] valuesStr = Arrays.stream(valuesStrUpper).map(s -> lowerCase ? s.toLowerCase() : s).toArray(String[]::new);

                // allow upgrading boolean to string list
                ConfigCategory cat = config.getCategory(configEnum.cat());
                Property oldProp = cat.get(field.getName());
                String oldVal = null;
                if(oldProp != null && oldProp.getType() != Type.STRING) {
                    oldVal = oldProp.getString();
                    cat.remove(field.getName());
                }

                String newValueStr = config.getString(field.getName(), configEnum.cat(),
                        lowerCase ? configEnum.def().toLowerCase() : configEnum.def().toUpperCase(), configEnum.com(), valuesStr);
                if(oldVal != null) {
                    newValueStr = oldVal;
                }
                if(!enumMap.containsKey(newValueStr.toUpperCase())) {
                    newValueStr = configEnum.def().toUpperCase();
                    if(lowerCase) {
                        newValueStr = newValueStr.toLowerCase();
                    }
                }
                newValue = enumMap.get(newValueStr.toUpperCase());

                Property newProp = cat.get(field.getName());
                if(!newProp.getString().equals(newValueStr)) {
                    newProp.set(newValueStr);
                }
            }

            if(needsReload != null && !newValue.equals(currentValue)) {
                needReload = true;
            }

            try {
                field.set(null, newValue);
            } catch (Exception e) {
                LOGGER.error("Failed to set value of field " + field.getName());
                e.printStackTrace();
            }
        }

        return needReload;
    }*/
    
    public static boolean reloadIfChanged(ReloadInfo info) {
//        if(watcher != null) {
//            WatchKey key = watcher.poll();
//
//            if(key != null) {
//                for(WatchEvent<?> event: key.pollEvents()) {
//                    if(event.context().toString().equals(configFile.getName())) {
//                        //reloadConfig(info);
//                        reloaded = true;
//                    }
//                }
//                key.reset();
//            }
//        }
        return info.needReload;
    }

//    private static void registerWatchService() throws IOException {
//        watcher = FileSystems.getDefault().newWatchService();
//        configFile.toPath().getParent().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
//    }

//    public static List<HumanReadableConfigElement> getElements() {
//        List<HumanReadableConfigElement> list = new ArrayList<>();
//        for(String prop : config.get("render").values()) {
//            list.add(new HumanReadableConfigElement(prop));
//        }
//        return list;
//    }

//    @Override
//    public void preInitialize() {
//        this.registerProperties();
//    }

//    @Override
//    public void initialize() {
//        instance = this;
//    }
    
    /*public static void flush() {
        if(config.hasChanged()) {
            config.save();
        }
    }*/
    
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public static @interface NeedsReload {
//
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public static @interface ConfigBoolean {
//
//        String cat();
//        boolean def();
//        String com() default "";
//
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public static @interface ConfigInt {
//
//        String cat();
//        int min();
//        int max();
//        int def();
//        String com() default "";
//
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public static @interface ConfigEnum {
//
//        String cat();
//        String def();
//        String com() default "";
//        Class<? extends Enum> clazz();
//
//    }
//
    public static class ReloadInfo {
        public boolean needReload;
    }
//
//    public enum AutomatableBoolean {
//        FALSE, TRUE, AUTO
//    }
    
}
