package org.koteyka.mytablist.manager;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.koteyka.mytablist.MyTabList;
import org.koteyka.mytablist.utils.TextUtils;

import java.util.ArrayList;
import java.util.SortedMap;

public class TabManager {

    private final MyTabList plugin;

    private enum FieldLP {
        PREFIX,
        SUFFIX;
    }

    public TabManager(MyTabList plugin) {
        this.plugin = plugin;
    }

    public void update(Player player) {
        if (getPropertyBoolean("enabled", "tab")) {
            String fullNicknameTab = getFullNickname(player, "tab");
            player.setPlayerListName(TextUtils.applyStyle(fullNicknameTab));
        }
        if (getPropertyBoolean("enabled", "chat_name")) {
            String fullNicknameChat = getFullNickname(player, "chat_name");
            player.setDisplayName(TextUtils.applyStyle(fullNicknameChat));
        }
    }

    private String getFullNickname(Player player, String position) {
        String rawPattern = getProperty("final", position.toLowerCase());
        String teamReplacedPattern = replaceTeam(rawPattern, player);
        return replacePlaceHolder(replacePlaceHolder(teamReplacedPattern, player), player);
    }

    private String replacePlaceHolder(String text, Player player) {
        return text
                .replace("{lp-prefix}", getFieldLP(player, FieldLP.PREFIX))
                .replace("{p-gamemode}", getGameMode(player, "prefix"))
                .replace("{p-world}", getWorld(player, "prefix"))
                .replace("{p-op}", getOp(player, "prefix"))

                .replace("{player}", player.getName())

                .replace("{s-gamemode}", getGameMode(player, "suffix"))
                .replace("{s-world}", getWorld(player, "suffix"))
                .replace("{s-op}", getOp(player, "suffix"))
                .replace("{lp-suffix}", getFieldLP(player, FieldLP.SUFFIX));
    }

    private String replaceTeam(String pattern, Player player) {
        Team team = player.getScoreboard().getEntryTeam(player.getName());
        String prefix = "";
        String suffix = "";
        if (team != null) {
            prefix = team.getPrefix();
            suffix = team.getSuffix();
        }
        return pattern.replace("{prefix}", prefix).replace("{suffix}", suffix);
    }

    private String getOp(Player player, String pos) {
        return getProperty(pos, (player.isOp() ? "op" : "noop"));
    }

    private String getWorld(Player player, String pos) {
        return getProperty(pos, player.getWorld().getEnvironment().name().toLowerCase());
    }

    private String getGameMode(Player player, String pos) {
        return getProperty(pos, player.getGameMode().name().toLowerCase());
    }

    private String getProperty(String... property) {
        return plugin.config().getString(property)
                .replace("§", "&");
    }

    private boolean getPropertyBoolean(String... property) {
        return plugin.config().getBoolean(String.join(".", property));
    }

    private String makeMultiMeta(SortedMap<Integer, String> metaValue, String separator) {
        ArrayList<String> metaValues = new ArrayList<>();
        metaValue.forEach(((priority, value) -> {
            metaValues.add(value);
        }));
        return String.join(separator, metaValues);
    }

    private String getFieldLP(Player player, FieldLP fieldLP) {
        String result = "";
        if (checkLP(plugin)) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User userPlayer = luckPerms.getUserManager().getUser(player.getUniqueId());

            if (userPlayer != null) {
                CachedMetaData metaData = userPlayer.getCachedData().getMetaData();

                switch (fieldLP) {
                    case PREFIX:
                        if (!getPropertyBoolean("lp", "multi_prefix_enabled")) {
                            result = metaData.getPrefix();
                        } else {
                            String separator = getProperty("lp", "multi_prefix_separator");
                            SortedMap<Integer, String> prefixes = metaData.getPrefixes();
                            result = makeMultiMeta(prefixes, separator);
                        }
                        break;
                    case SUFFIX:
                        if (!getPropertyBoolean("lp", "multi_suffix_enabled")) {
                            result = metaData.getSuffix();
                        } else {
                            String separator = getProperty("lp", "multi_suffix_separator");
                            SortedMap<Integer, String> suffixes = metaData.getSuffixes();
                            result = makeMultiMeta(suffixes, separator);
                        }
                        break;
                }
            }
        }
        if (result == null) {
            result = "";
        }
        return result;
    }

    public static boolean checkLP(JavaPlugin plugin) {
        Plugin luckPerms = plugin.getServer().getPluginManager().getPlugin("LuckPerms");
        return luckPerms != null;
    }
}
