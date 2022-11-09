package com.sucy.party.command;

import com.sucy.party.Parties;
import com.sucy.party.Party;
import com.sucy.party.lang.ErrorNodes;
import com.sucy.party.lang.IndividualNodes;
import mc.promcteam.engine.mccore.commands.ConfigurableCommand;
import mc.promcteam.engine.mccore.commands.IFunction;
import mc.promcteam.engine.mccore.config.CustomFilter;
import mc.promcteam.engine.mccore.util.TextSizer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * Command to display party information
 */
public class CmdInfo implements IFunction {

    /**
     * Executes the command
     *
     * @param command owning command
     * @param plugin  plugin reference
     * @param sender  sender of the command
     * @param args    arguments provided
     */
    @Override
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args) {

        Parties parties = (Parties) plugin;
        Player  player  = (Player) sender;

        // Check the sender's party status
        Party party = parties.getParty(player);
        if (party != null && party.isMember(player)) {
            StringBuilder members = new StringBuilder();
            for (UUID id : party.getMembers()) {
                members.append(Bukkit.getOfflinePlayer(id).getName());
                members.append(", ");
            }
            parties.sendMessage(
                    player,
                    IndividualNodes.INFO,
                    new CustomFilter("{leader}", party.getLeader().getName()),
                    new CustomFilter("{members}", members.substring(0, members.length() - 2)),
                    new CustomFilter("{size}", party.getPartySize() + ""),
                    new CustomFilter("{break}", TextSizer.createLine("", "-", ChatColor.DARK_GRAY))
            );
        } else {
            parties.sendMessage(player, ErrorNodes.NO_PARTY);
        }
    }
}
