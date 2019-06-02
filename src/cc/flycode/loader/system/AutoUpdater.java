package cc.flycode.loader.system;

import cc.flycode.loader.ILoaderPlugin;
import cc.flycode.loader.plugin.InjectedPlugin;
import cc.flycode.loader.util.HTTPConnectionUtil;
import cc.flycode.loader.util.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/**
 * Created by FlyCode on 02/06/2019 Package cc.flycode.loader.system
 */
public class AutoUpdater {
    public AutoUpdater() {
        ILoaderPlugin.instance.loader.getExecutorService().scheduleAtFixedRate(() -> ILoaderPlugin.instance.loader.getInjectedPlugins().forEach((b, p) -> {
            if (p != null && Bukkit.getServer().getPluginManager().getPlugin(p.pluginName).isEnabled()) {
                if (resolveVersionURL(b) != null) {
                    String res = HTTPConnectionUtil.getResponse(resolveVersionURL(b));
                    if (res != null) {
                        if (!res.equalsIgnoreCase(Bukkit.getServer().getPluginManager().getPlugin(p.pluginName).getDescription().getVersion())) {
                            ILoaderPlugin.instance.getLogger().info("An update has been found for: " + b + " updating it now!");
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    InjectedPlugin injectedPlugin = ILoaderPlugin.instance.loader.getInjectedPlugins().get(b);
                                    Plugin plugin = Bukkit.getPluginManager().getPlugin(injectedPlugin.pluginName);
                                    if (plugin != null) {
                                        new PluginUtils().unloadPlugin(injectedPlugin.pluginName);
                                        Bukkit.getPluginManager().disablePlugin(plugin);
                                        ILoaderPlugin.instance.loader.getInjectedPlugins().remove(b);
                                        ILoaderPlugin.instance.loader.loadPlugin(b);
                                    }
                                }
                            }.runTask(ILoaderPlugin.instance);
                        }
                    }
                }
            }
        }), 20L, 20L, TimeUnit.SECONDS);
    }

    private String resolveVersionURL(String pluginName) {
        switch (pluginName) {
            //add plugins here follow the current format make sure its all lowercase and replace the names
            case "iutils":
                return "http://95.216.193.177/pluginData.php?name=iutils";
            case "ihub":
                return "http://95.216.193.177/pluginData.php?name=ihub";
        }
        return null;
    }
}
