package org.koteyka.mytablist.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.koteyka.mytablist.MyTabList;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {

    private final MyTabList plugin;

    public ReloadCommand(MyTabList plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mytablist.cmd.reload")) {
            return true;
        }
        plugin.config().reloadConfig();
        sender.sendMessage(ChatColor.DARK_GREEN + "Reloaded");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }

    public static void register(MyTabList plugin) {
        ReloadCommand reloadCommand = new ReloadCommand(plugin);
        plugin.getCommand("tabreload").setExecutor(reloadCommand);
        plugin.getCommand("tabreload").setTabCompleter(reloadCommand);
    }
}
