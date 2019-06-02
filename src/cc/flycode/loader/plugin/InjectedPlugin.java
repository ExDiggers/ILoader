package cc.flycode.loader.plugin;

import cc.flycode.loader.ILoaderPlugin;
import lombok.Getter;
import cc.flycode.loader.system.Loader;
import me.mat1337.loader.accessor.FieldAccess;
import me.mat1337.loader.accessor.Instance;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Getter
public class InjectedPlugin {

    private File pluginFile;
    private File root;
    private Plugin handle;
    public String pluginName;

    public InjectedPlugin(File pluginFile) {
        this.pluginFile = pluginFile;
        Arrays.stream(Objects.requireNonNull(this.pluginFile.getParentFile().listFiles())).forEach(file -> {
            String name = file.getName();
            if (name.startsWith(Loader.prefix) && name.endsWith(".jar") && !file.equals(this.pluginFile)) {
                file.delete();
            }
        });
    }

    public void deletePlugin() {
        pluginFile.delete();
    }

    public void setHandle(Plugin parent) {
        handle = parent;
        fixPluginDir(parent);
        fixConfig();
        checkFields(parent);
    }

    public void enablePlugin(Plugin parent) {
        try {
            handle = parent.getPluginLoader().loadPlugin(pluginFile);
        } catch (InvalidPluginException e) {
            e.printStackTrace();
        }
        fixPluginDir(parent);
        fixConfig();
        checkFields(parent);
        parent.getServer().getPluginManager().enablePlugin(handle);
    }

    public void overWrite(String pluginName) {
        List<String> lines = Collections.singletonList(UUID.randomUUID().toString());
        Path file = Paths.get(String.valueOf(ILoaderPlugin.instance.loader.getInjectedPlugins().get(pluginName).getPluginFile()));
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disablePlugin() {
        unload();
        if (pluginFile.exists())
            pluginFile.delete();

    }

    /**
     * Credit from PlugMan github
     * (Cleaned up the code)
     * <p>
     * https://github.com/r-clancy/PlugMan/blob/master/src/main/java/com/rylinaux/plugman/util/PluginUtil.java
     */

    public void unload() {

        String name = handle.getName();

        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.disablePlugin(handle);

        List<Plugin> plugins = new FieldAccess(pluginManager.getClass(), "plugins").read(pluginManager);
        Map<String, Plugin> names = new FieldAccess(pluginManager.getClass(), "lookupNames").read(pluginManager);
        SimpleCommandMap commandMap = new FieldAccess(pluginManager.getClass(), "commandMap").read(pluginManager);
        Map<String, Command> commands = new FieldAccess(SimpleCommandMap.class, "knownCommands").read(commandMap);

        pluginManager.disablePlugin(handle);

        if (plugins != null)
            plugins.remove(handle);

        if (names != null)
            names.remove(name);

        if (commandMap != null) {
            for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand c = (PluginCommand) entry.getValue();
                    if (c.getPlugin() == handle) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
        }

        ClassLoader cl = handle.getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            new FieldAccess(cl.getClass(), "plugin").set(cl, null);
            new FieldAccess(cl.getClass(), "pluginInit").set(cl, null);
            closeLoader((URLClassLoader) cl);
        }

    }

    private void checkFields(Plugin parent) {
        Arrays.stream(handle.getClass().getFields()).filter(field -> field.isAnnotationPresent(Instance.class)).forEach(field -> {
            try {
                field.setAccessible(true);
                field.set(handle, parent);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        Arrays.stream(handle.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Instance.class)).forEach(field -> {
            try {
                field.setAccessible(true);
                field.set(handle, parent);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private void fixConfig() {
        File file;
        new FieldAccess(JavaPlugin.class, "configFile").set(handle, (file = new File(root, "config.yml")));
        new FieldAccess(JavaPlugin.class, "newConfig").set(handle, YamlConfiguration.loadConfiguration(file));
    }

    private void fixPluginDir(Plugin parent) {
        new FieldAccess(JavaPlugin.class, "dataFolder").set(handle, (root = new File(parent.getDataFolder().getParentFile(), handle.getDescription().getName().replaceAll(" ", "_"))));
    }

    private void closeLoader(URLClassLoader classLoader) {
        try {
            classLoader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
