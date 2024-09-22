package makamys.neodymium.compat;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import makamys.neodymium.config.NeodymiumConfig;

public class NeodymiumModMenu
        implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return s -> NeodymiumConfig.getInstance().getConfigScreen(s);
    }
}
