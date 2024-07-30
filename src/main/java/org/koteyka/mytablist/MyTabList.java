package org.koteyka.mytablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.koteyka.mytablist.command.ReloadCommand;
import org.koteyka.mytablist.manager.TabManager;
import org.koteyka.mytablist.utils.Config;

public final class MyTabList extends JavaPlugin {

    private Config config;

    @Override
    public void onEnable() {
        config = new Config(this);
        config.generateConfig();
        ReloadCommand.register(this);
        register();
    }

    private void register() {
        TabManager tab = new TabManager(this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                tab.update(onlinePlayer);
            }
        }, 0L, 10L);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling!!");
    }

    public Config config() {
        return config;
    }
}
