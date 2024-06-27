package makamys.neodymium.config;

import fi.dy.masa.malilib.config.interfaces.IConfigBase;
import fi.dy.masa.malilib.config.interfaces.IValueChangeCallback;
import makamys.neodymium.Neodymium;

import java.util.Timer;
import java.util.TimerTask;

public class ValueChangeCallback implements IValueChangeCallback {

    private Timer timer;

    public ValueChangeCallback() {
        timer = new Timer();
    }

    @Override
    public void onValueChanged(IConfigBase iConfigBase) {

        // 每次调用 valueChanged 方法都取消之前的任务
        timer.cancel();
        timer = new Timer();

        // 时间间隔，单位为毫秒
        int delay = 1000;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onValueChanged();
            }
        }, delay);
    }

    private void onValueChanged() {
        if (!Neodymium.CONFIG_RELOAD_INFO.needReload) {
            Neodymium.CONFIG_RELOAD_INFO.needReload = true;
        }
    }
}
