package makamys.neodymium.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;

import javax.annotation.Nullable;

public class ConfigBooleanChangeCallback extends ConfigBoolean {
    @Nullable
    private ValueChangeCallback callback = new ValueChangeCallback();

    public ConfigBooleanChangeCallback(String name) {
        super(name);
    }

    public ConfigBooleanChangeCallback(String name, String comment) {
        super(name, comment);
    }

    public ConfigBooleanChangeCallback(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    public ConfigBooleanChangeCallback(String name, boolean defaultValue, String comment) {
        super(name, defaultValue, comment);
    }

    @Override
    public void onValueChanged() {
        if (this.callback != null) {
            this.callback.onValueChanged(this);
        }

    }
}
