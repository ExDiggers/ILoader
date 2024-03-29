package cc.flycode.loader.command;

import cc.flycode.loader.ILoaderPlugin;
import cc.flycode.loader.plugin.InjectedPlugin;
import cc.flycode.loader.util.HTTPConnectionUtil;
import cc.flycode.loader.util.PluginUtils;
import cc.flycode.loader.util.file.PluginDataFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by FlyCode on 02/06/2019 Package cc.flycode.loader.command
 */
public class ILoaderCommand implements CommandExecutor {
    private String prefix = ChatColor.GRAY+"["+ ChatColor.AQUA+"ILoader"+ ChatColor.GRAY+"]";
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equalsIgnoreCase("iloader")) {
            if (commandSender.isOp() || commandSender.hasPermission("iloader.command")) {
                if (strings.length > 0) {
                    try {
                        if (strings[0].equalsIgnoreCase("list")) {
                            commandSender.sendMessage(prefix + " " + ChatColor.YELLOW + "Here is a list of all of the plugins that are enabled / disabled:");
                            ILoaderPlugin.instance.loader.getPluginList().forEach(plugins -> {
                                commandSender.sendMessage(ChatColor.GRAY + "> " + (ILoaderPlugin.instance.loader.getInjectedPlugins().containsKey(plugins.toLowerCase()) ? ChatColor.GREEN : ChatColor.RED) + plugins);
                            });
                            return true;
                        }

                        if (strings[0].equalsIgnoreCase("install")) {
                            try {
                                String pName = strings[1].toLowerCase();
                                ILoaderPlugin.instance.loader.getExecutorService().execute(() -> {
                                    if (ILoaderPlugin.instance.loader.isValidPlugin(pName)) {
                                        String res = HTTPConnectionUtil.getResponse(ILoaderPlugin.instance.loader.getDownloadURL(pName).replaceFirst("download.php","downloadKeyCheck.php"));
                                        if (!res.equalsIgnoreCase("PASSED")) {
                                            commandSender.sendMessage(prefix + " " + ChatColor.RED + "Looks like your key is invalid for the plugin: " + ChatColor.GRAY+pName+""+ ChatColor.RED+"!");
                                            return;
                                        }
                                        PluginDataFile.getInstance().setup(ILoaderPlugin.instance);
                                        List<String> active = PluginDataFile.getInstance().getData().getStringList("active");
                                        if (!active.contains(pName)) {
                                            active.add(pName);
                                            PluginDataFile.getInstance().getData().set("active", active);
                                            PluginDataFile.getInstance().saveData();
                                            ILoaderPlugin.instance.loader.loadPlugin(pName);
                                            commandSender.sendMessage(prefix + " " + ChatColor.GREEN + " Downloaded & Injected the plugin: " + ChatColor.GRAY + pName + ChatColor.GREEN + "!");
                                        } else {
                                            commandSender.sendMessage(prefix + " " + ChatColor.YELLOW + pName + " " + ChatColor.RED + "is already loaded!");
                                        }
                                    } else {
                                        commandSender.sendMessage(prefix + " " + ChatColor.GRAY + " Could not find the plugin by the name: " + ChatColor.RED + pName + ChatColor.GRAY + "!");
                                    }
                                });
                            } catch (Exception ignored) {
                                commandSender.sendMessage(prefix + " " + ChatColor.RED + " Enter a valid plugin name!");
                            }
                        }

                        if (strings[0].equalsIgnoreCase("uninstall")) {
                            try {
                                String pName = strings[1].toLowerCase();
                                ILoaderPlugin.instance.loader.getExecutorService().execute(() -> {
                                    if (ILoaderPlugin.instance.loader.isValidPlugin(pName)) {
                                        PluginDataFile.getInstance().setup(ILoaderPlugin.instance);
                                        List<String> active = PluginDataFile.getInstance().getData().getStringList("active");
                                        if (active.contains(pName) && ILoaderPlugin.instance.loader.getInjectedPlugins().containsKey(pName)) {
                                            active.remove(pName);
                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    InjectedPlugin injectedPlugin = ILoaderPlugin.instance.loader.getInjectedPlugins().get(pName);
                                                    injectedPlugin.disablePlugin();
                                                    ILoaderPlugin.instance.loader.getInjectedPlugins().remove(pName);
                                                  /*  Plugin plugin = Bukkit.getPluginManager().getPlugin(injectedPlugin.pluginName);
                                                    if (plugin != null) {
                                                        injectedPlugin.disablePlugin();
                                                        injectedPlugin.overWrite(pName);
                                                        ILoaderPlugin.instance.loader.getInjectedPlugins().remove(pName);
                                                    }*/
                                                }
                                            }.runTask(ILoaderPlugin.instance);
                                            PluginDataFile.getInstance().getData().set("active", active);
                                            PluginDataFile.getInstance().saveData();

                                            commandSender.sendMessage(prefix + " " + ChatColor.GREEN + " Removing & Un-Injecting the plugin: " + ChatColor.GRAY + pName + ChatColor.GREEN + "!");
                                        } else {
                                            commandSender.sendMessage(prefix + " " + ChatColor.YELLOW + pName + " " + ChatColor.RED + "was not found in the active plugins list!");
                                        }
                                    } else {
                                        commandSender.sendMessage(prefix + " " + ChatColor.GRAY + " Could not find the plugin by the name: " + ChatColor.RED + pName + ChatColor.GRAY + "!");
                                    }
                                });
                            } catch (Exception ignored) {
                                commandSender.sendMessage(prefix + " " + ChatColor.RED + " Enter a valid plugin name!");
                            }
                        }

                    } catch (Exception ignored) {
                        sendUsage(commandSender);
                    }
                } else {
                    sendUsage(commandSender);
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "No Permission!");
            }
        }
        return true;
    }
    private void sendUsage(CommandSender commandSender) {
        commandSender.sendMessage(prefix + " " + ChatColor.RED + "Incorrect usage!");
        commandSender.sendMessage(ChatColor.GRAY + "/ILoader install <plugin>");
        commandSender.sendMessage(ChatColor.GRAY + "/ILoader uninstall <plugin>");
        commandSender.sendMessage(ChatColor.GRAY + "/ILoader list");
    }
}
