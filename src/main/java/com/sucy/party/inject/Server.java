package com.sucy.party.inject;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Parties © 2023
 * com.sucy.party.inject.Server
 * <p>
 * Wrapper around static methods than can have alternate implementations
 * injected for testing purposes.
 */
public class Server {

    public static PlayerData getPlayerData(Player player) {
        return SkillAPI.getPlayerData(player);
    }

    public static PlayerClass getClass(Player player) {
        return getPlayerData(player).getMainClass();
    }

    public static int getLevel(UUID id) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(id);
        if (op.isOnline()) {
            PlayerClass playerClass = getClass(op.getPlayer());
            if (playerClass != null) {
                return playerClass.getLevel();
            }
        }
        return 0;
    }

    public static boolean hasClass(Player player) {
        return getPlayerData(player).hasClass();
    }

}
