package cc.flycode.loader.util.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;

public class KeyFile {

    private KeyFile() {
    }

    static KeyFile instance = new KeyFile();

    public static KeyFile getInstance() {
        return instance;
    }

    Plugin p;

    FileConfiguration config;
    File cfile;

    FileConfiguration data;
    File dfile;

    public void setup(Plugin p) {
        config = p.getConfig();
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }
        dfile = new File(p.getDataFolder(), "Licenses.yml");

        if (!dfile.exists()) {
            try {
                dfile.createNewFile();
            } catch (IOException e) {
            }
        }

        data = YamlConfiguration.loadConfiguration(dfile);

        writeDefaults();

    }

    public FileConfiguration getData() {
        return data;
    }

    public void writeDefaults() {
        if (!data.contains("Licenses.iUtils")) data.set("Licenses.iUtils", "Enter_Key_Here");
        if (!data.contains("Licenses.iHub")) data.set("Licenses.iHub", "Enter_Key_Here");
        saveData();
    }


    public void saveData() {
        try {
            data.save(dfile);
        } catch (IOException e) {
        }
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(dfile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(cfile);
        } catch (IOException e) {
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public PluginDescriptionFile getDesc() {
        return p.getDescription();
    }
}