package com.sucy.party.mccore;

import com.sucy.party.Parties;
import com.sucy.party.lang.PartyNodes;
import mc.promcteam.engine.mccore.scoreboard.BoardManager;
import mc.promcteam.engine.mccore.scoreboard.PlayerBoards;
import mc.promcteam.engine.mccore.scoreboard.StatBoard;
import org.bukkit.entity.Player;

/**
 * Manages giving and removing scoreboards to players through MCCore
 */
public class PartyBoardManager {

    /**
     * Applies a scoreboard for the player
     *
     * @param plugin plugin reference
     * @param player player to apply to
     */
    public static void applyBoard(Parties plugin, Player player) {
        if (!plugin.isUsingScoreboard()) {
            return;
        }

        String title = plugin.getMessage(PartyNodes.SCOREBOARD, false).get(0);

        StatBoard board = new StatBoard(title, plugin.getName());
        board.addStats(new PartyStats(plugin, player, plugin.isLevelScoreboard()));
        PlayerBoards boards = BoardManager.getPlayerBoards(player.getName());
        boards.removeBoards(plugin.getName());
        boards.addBoard(board);
    }

    /**
     * Removes boards for this plugin on empty party
     *
     * @param plugin plugin reference
     * @param player player to remove for
     */
    public static void clearBoard(Parties plugin, Player player) {
        if (!plugin.isUsingScoreboard()) {
            return;
        }
        BoardManager.getPlayerBoards(player.getName()).removeBoards(plugin.getName());
    }

    /**
     * Clears all of the scoreboards for the plugin
     *
     * @param plugin plugin reference
     */
    public static void clearBoards(Parties plugin) {
        if (!plugin.isUsingScoreboard()) {
            return;
        }
        BoardManager.clearPluginBoards(plugin.getName());
    }
}
