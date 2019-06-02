package cc.flycode.loader.config;

import cc.flycode.loader.ILoaderPlugin;
import cc.flycode.loader.util.file.KeyFile;

/**
 * Created by FlyCode on 02/06/2019 Package cc.flycode.loader.config
 */
public class ConfigManager {
    public void load() {
        KeyFile.getInstance().setup(ILoaderPlugin.instance);
        ConfigValues.IOUtilsKey = KeyFile.getInstance().getData().getString("Licenses.IUtils");
    }
}
