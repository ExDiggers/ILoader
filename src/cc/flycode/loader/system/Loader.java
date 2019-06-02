package cc.flycode.loader.system;

import cc.flycode.loader.ILoaderPlugin;
import cc.flycode.loader.command.ILoaderCommand;
import cc.flycode.loader.config.ConfigValues;
import cc.flycode.loader.downloader.FileDownloader;
import cc.flycode.loader.plugin.InjectedPlugin;
import cc.flycode.loader.util.HTTPConnectionUtil;
import cc.flycode.loader.util.file.PluginDataFile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Loader {

    public static String prefix = "mldr";

    public static ILoaderPlugin INSTANCE;

    @Getter
    private ScheduledExecutorService executorService;

    @Getter
    private List<String> pluginList = new ArrayList<>();

    @Getter
    private List<File> pluginFile = new ArrayList<>();

    @Getter
    private HashMap<String, InjectedPlugin> injectedPlugins = new HashMap<>();

    public void start() {
        executorService = Executors.newSingleThreadScheduledExecutor();


        //add all plugins here
        pluginList.add("iUtils");
        pluginList.add("iHub");

        executorService.execute(() -> {
            List<String> shouldLoad;
            PluginDataFile.getInstance().setup(INSTANCE);
            shouldLoad = PluginDataFile.getInstance().getData().getStringList("active");
            shouldLoad.forEach(this::loadPlugin);
        });
        Bukkit.getPluginCommand("iloader").setExecutor(new ILoaderCommand());
        new AutoUpdater();
    }

    public void shutdown() {
        injectedPlugins.forEach((p, b) -> {
            if (b != null) {
                b.disablePlugin();
                b.overWrite(p);
            }
        });
        executorService.shutdownNow();
    }


    public void reloadPlugin(String pluginName) {
        if (injectedPlugins.containsKey(pluginName)) {
            if (injectedPlugins.get(pluginName) != null) {
                injectedPlugins.get(pluginName).disablePlugin();
                loadPlugin(pluginName);
            }
        }
    }

    public void disablePlugin(String pluginName) {
        if (injectedPlugins.containsKey(pluginName)) {
            injectedPlugins.get(pluginName).disablePlugin();
        }
    }

    public void loadPlugin(String pluginName) {
        if (!injectedPlugins.containsKey(pluginName)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    InjectedPlugin temp;
                    String URL = getDownloadURL(pluginName);
                    if (URL.equalsIgnoreCase("(error)")) {
                        ILoaderPlugin.instance.getLogger().info("Error injecting the plugin - " + pluginName + " (cannot validate key)");
                        return;
                    }
                    /*temp = new InjectedPlugin(new FileDownloader(URL).download());
                    File file = temp.getPluginFile();
                    try {
                        injectedPlugins.put(pluginName, temp);
                        Plugin load = Bukkit.getServer().getPluginManager().loadPlugin(file);
                        injectedPlugins.get(pluginName).pluginName = load.getDescription().getName();
                        injectedPlugins.get(pluginName).setHandle(load);
                        injectedPlugins.get(pluginName).fixPluginDir(load);
                        injectedPlugins.get(pluginName).fixConfig();
                        injectedPlugins.get(pluginName).checkFields(load);

                        Bukkit.getPluginManager().enablePlugin(load);




                        pluginFile.add(file);
                        ILoaderPlugin.instance.getLogger().info("Downloaded & Injected " + pluginName);
                    } catch (InvalidPluginException | InvalidDescriptionException e) {
                        e.printStackTrace();
                    }*/

                    InjectedPlugin plugin;
                    plugin = new InjectedPlugin(new FileDownloader(URL).download());
                    injectedPlugins.put(pluginName, plugin);
                    injectedPlugins.get(pluginName).enablePlugin(ILoaderPlugin.instance);
                    pluginFile.add(plugin.getPluginFile());
                    ILoaderPlugin.instance.getLogger().info("Downloaded & Injected " + pluginName);

                }
            }.runTask(ILoaderPlugin.instance);
        }
    }

    public String getDownloadURL(String pluginName) {
        String rand = ALAPI.toBinary(UUID.randomUUID().toString()), sKey = ALAPI.toBinary(ALAPI.KEY), downloadURL = null, checkURL;
        switch (pluginName.toLowerCase()) {

            //add plugins here follow the current format make sure its all lowercase and replace the names

            case "iutils":
                downloadURL = "http://95.216.193.177/download.php" + "?v1=" + ALAPI.xor(rand, sKey) + "&v2=" + ALAPI.xor(rand, ALAPI.toBinary(ConfigValues.IOUtilsKey)) + "&pl=iUtils";
                checkURL = downloadURL.replaceFirst("download.php", "downloadKeyCheck.php");
                if (!HTTPConnectionUtil.getResponse(checkURL).equalsIgnoreCase("PASSED")) {
                    downloadURL = "(ERROR)";
                }
                break;

            case "ihub":
                downloadURL = "http://95.216.193.177/download.php" + "?v1=" + ALAPI.xor(rand, sKey) + "&v2=" + ALAPI.xor(rand, ALAPI.toBinary(ConfigValues.IOHubKey)) + "&pl=iHub";
                checkURL = downloadURL.replaceFirst("download.php", "downloadKeyCheck.php");
                if (!HTTPConnectionUtil.getResponse(checkURL).equalsIgnoreCase("PASSED")) {
                    downloadURL = "(ERROR)";
                }
                break;


        }
        return downloadURL;
    }

    public boolean isValidPlugin(String pluginName) {
        return (pluginName.toLowerCase().equalsIgnoreCase("iutils") || pluginName.toLowerCase().equalsIgnoreCase("ihub"));
    }
}
