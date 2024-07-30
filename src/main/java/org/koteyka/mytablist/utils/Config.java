package org.koteyka.mytablist.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class Config {

    private FileConfiguration fileConfiguration;
    private final JavaPlugin plugin;

    public Config(JavaPlugin plugin) {
        fileConfiguration = plugin.getConfig();
        this.plugin = plugin;
    }

    public int getInteger(String... path) {
        return fileConfiguration.getInt(String.join(".", path));
    }

    public List<Integer> getIntegerList(String... path) {
        return fileConfiguration.getIntegerList(String.join(".", path));
    }

    public double getDouble(String... path) {
        return fileConfiguration.getDouble(String.join(".", path));
    }

    public List<Double> getDoubleList(String... path) {
        return fileConfiguration.getDoubleList(String.join(".", path));
    }

    public boolean getBoolean(String... path) {
        return fileConfiguration.getBoolean(String.join(".", path));
    }

    public List<Boolean> getBooleanList(String... path) {
        return fileConfiguration.getBooleanList(String.join(".", path));
    }

    public String getString(String... path) {
        return fileConfiguration.getString(String.join(".", path));
    }

    public List<String> getStringList(String... path) {
        return fileConfiguration.getStringList(String.join(".", path));
    }

    public void generateConfig() {
        reloadConfig();

        // PREFIX
        fileConfiguration.addDefault("prefix.creative", "§6[C]");
        fileConfiguration.addDefault("prefix.spectator", "§b[S]");
        fileConfiguration.addDefault("prefix.adventure", "§d[A]");
        fileConfiguration.addDefault("prefix.survival", "§c[S]");

        fileConfiguration.addDefault("prefix.normal", "<gr #3ffe3f:#00be00>{player}</gr>");
        fileConfiguration.addDefault("prefix.nether", "<gr #fe3f3f:#be0000>{player}</gr>");
        fileConfiguration.addDefault("prefix.the_end", "<gr #fe3ffe:#be00be>{player}</gr>");
        fileConfiguration.addDefault("prefix.custom", "");

        fileConfiguration.addDefault("prefix.op", "#8383FF*");
        fileConfiguration.addDefault("prefix.noop", "");

        // SUFFIX
        fileConfiguration.addDefault("suffix.creative", "");
        fileConfiguration.addDefault("suffix.spectator", "");
        fileConfiguration.addDefault("suffix.adventure", "");
        fileConfiguration.addDefault("suffix.survival", "");

        fileConfiguration.addDefault("suffix.normal", "");
        fileConfiguration.addDefault("suffix.nether", "");
        fileConfiguration.addDefault("suffix.the_end", "");
        fileConfiguration.addDefault("suffix.custom", "");

        fileConfiguration.addDefault("suffix.op", "");
        fileConfiguration.addDefault("suffix.noop", "");

        // ENABLED
        fileConfiguration.addDefault("enabled.tab", true);
        fileConfiguration.addDefault("enabled.chat_name", true);

        // FINAL
        fileConfiguration.addDefault("final.tab", "{p-gamemode} §r{lp-prefix}§r{prefix}§r{p-op}§r{p-world}§r{suffix}§r{s-op}§r{lp-suffix}");
        fileConfiguration.addDefault("final.chat_name", "§r{prefix}§r{p-op}§r{p-world}§r{suffix}§r{s-op}§r{lp-suffix}");

        fileConfiguration.addDefault("lp.multi_prefix_enabled", true);
        fileConfiguration.addDefault("lp.multi_prefix_separator", "§r");

        fileConfiguration.addDefault("lp.multi_suffix_enabled", true);
        fileConfiguration.addDefault("lp.multi_suffix_separator", "§r");

        fileConfiguration.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public void reloadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        fileConfiguration = plugin.getConfig();
    }
}
