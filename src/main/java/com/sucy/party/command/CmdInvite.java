package com.sucy.party.command;

import com.sucy.party.Parties;
import com.sucy.party.Party;
import com.sucy.party.lang.ErrorNodes;
import com.sucy.party.lang.IndividualNodes;
import com.sucy.party.lang.PartyNodes;
import mc.promcteam.engine.mccore.commands.ConfigurableCommand;
import mc.promcteam.engine.mccore.commands.IFunction;
import mc.promcteam.engine.mccore.config.Filter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Command to invite other players to a party
 */
public class CmdInvite implements IFunction {

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
        Player player = (Player) sender;

        // Requires at least one argument
        if (args.length == 0) {
            command.displayHelp(sender, 1);
            return;
        }

        // Cannot be yourself
        if (args[0].equalsIgnoreCase(player.getName())) {
            parties.sendMessage(player, ErrorNodes.NO_INVITE_SELF);
            return;
        }

        // Validate the player
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            parties.sendMessage(player, ErrorNodes.NOT_ONLINE);
            return;
        }

        // Check the sender's party status
        Party party = parties.getParty(player);
        if (party != null) {

            // Party is full
            if (party.isFull()) {
                parties.sendMessage(player, ErrorNodes.PARTY_FULL);
                return;
            }

            // Doesn't have permission
            if (parties.isLeaderInviteOnly() && !party.isLeader(player)) {
                parties.sendMessage(player, ErrorNodes.NOT_LEADER);
                return;
            }
        }

        // Check the target's party status
        Party targetParty = parties.getParty(target);
        if (targetParty != null && !targetParty.isEmpty()) {
            parties.sendMessage(player, ErrorNodes.IN_OTHER_PARTY);
            return;
        }

        // Clear the target's party
        if (targetParty != null) {
            parties.removeParty(targetParty);
        }

        // Initialize a new party if it doesn't exist
        if (party == null) {
            party = new Party(parties, player);
            parties.addParty(party);
        }

        // Invite the target
        party.invite(target);
        party.sendMessages(parties.getMessage(PartyNodes.PLAYER_INVITED, true, Filter.PLAYER.setReplacement(target.getName())));
        parties.sendMessage(target, IndividualNodes.INVITED, Filter.PLAYER.setReplacement(player.getName()));
    }
}
