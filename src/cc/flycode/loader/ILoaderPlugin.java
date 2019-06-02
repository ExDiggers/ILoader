package cc.flycode.loader;

import cc.flycode.loader.config.ConfigManager;
import cc.flycode.loader.system.Loader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by FlyCode on 02/06/2019 Package cc.flycode.loader.system
 */
public class ILoaderPlugin extends JavaPlugin {

    public Loader loader;
    public static ILoaderPlugin instance;

    @Override
    public void onEnable() {
        Loader.INSTANCE = this;
        instance = this;
        new ConfigManager().load();
        loader = new Loader();
        loader.start();
    }

    @Override
    public void onDisable() {
        loader.shutdown();
    }
}
