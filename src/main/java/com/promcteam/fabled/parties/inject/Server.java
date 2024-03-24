package com.promcteam.fabled.parties.inject;

import com.promcteam.fabled.Fabled;
import com.promcteam.fabled.api.player.PlayerClass;
import com.promcteam.fabled.api.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Parties © 2023
 * inject.com.promcteam.fabled.parties.Server
 * <p>
 * Wrapper around static methods than can have alternate implementations
 * injected for testing purposes.
 */
public class Server {

    public static PlayerData getPlayerData(Player player) {
        return Fabled.getPlayerData(player);
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
