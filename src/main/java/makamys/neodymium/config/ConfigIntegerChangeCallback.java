package makamys.neodymium.config;

import fi.dy.masa.malilib.config.options.ConfigInteger;

import javax.annotation.Nullable;

public class ConfigIntegerChangeCallback extends ConfigInteger {
    @Nullable
    private ValueChangeCallback callback = new ValueChangeCallback();

    public ConfigIntegerChangeCallback(String name, int defaultValue) {
        super(name, defaultValue);
    }

    public ConfigIntegerChangeCallback(String name, int defaultValue, String comment) {
        super(name, defaultValue, comment);
    }

    public ConfigIntegerChangeCallback(String name, int defaultValue, int minValue, int maxValue) {
        super(name, defaultValue, minValue, maxValue);
    }

    public ConfigIntegerChangeCallback(String name, int defaultValue, int minValue, int maxValue, String comment) {
        super(name, defaultValue, minValue, maxValue, comment);
    }

    @Override
    public void onValueChanged() {
        if (this.callback != null) {
            this.callback.onValueChanged(this);
        }

    }
}
