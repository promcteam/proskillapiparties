package com.sucy.party.mccore;

import com.sucy.party.Parties;
import com.sucy.party.Party;
import com.sucy.party.inject.Server;
import mc.promcteam.engine.mccore.scoreboard.StatHolder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Stat holder for a party scoreboard
 */
public class PartyStats implements StatHolder {

    private final Parties plugin;
    private final UUID    playerId;
    private final boolean level;

    /**
     * Constructor
     *
     * @param plugin plugin reference
     * @param player player to use
     * @param level  whether or to display level. False makes it display health
     */
    public PartyStats(Parties plugin, Player player, boolean level) {
        this.plugin = plugin;
        this.playerId = player.getUniqueId();
        this.level = level;
    }

    /**
     * @return stats map for a MCCore StatsScoreboard
     */
    @Override
    public List<String> getNames() {
        List<String>     stats  = new ArrayList<>();
        Optional<Player> player = getPlayer();
        if (player.isPresent()) {
            Party pt = plugin.getParty(player.get());
            if (pt != null && !pt.isEmpty()) {
                stats = pt.getMembers().stream().map(Bukkit::getOfflinePlayer)
                        .filter(OfflinePlayer::isOnline)
                        .map(OfflinePlayer::getName)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }
        return stats;
    }

    /**
     * @return the current values for the party members
     */
    @Override
    public List<Integer> getValues() {
        List<Integer>    stats  = new ArrayList<>();
        Optional<Player> player = getPlayer();
        if (player.isPresent()) {
            Party pt = plugin.getParty(player.get());
            if (pt != null && !pt.isEmpty()) {
                for (UUID id : pt.getMembers()) {
                    if (level) {
                        Server.getLevel(id);
                    } else {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(id);
                        if (op.isOnline()) {
                            stats.add((int) Math.ceil(op.getPlayer().getHealth()));
                        }
                    }
                }
            }
        }
        return stats;
    }

    public Optional<Player> getPlayer() {
        OfflinePlayer op = Bukkit.getOfflinePlayer(playerId);
        return op.isOnline()
                ? Optional.of(op.getPlayer())
                : Optional.empty();
    }
}
