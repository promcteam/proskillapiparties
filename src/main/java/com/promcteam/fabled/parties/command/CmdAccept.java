package com.promcteam.fabled.parties.command;

import com.promcteam.codex.mccore.commands.ConfigurableCommand;
import com.promcteam.codex.mccore.commands.IFunction;
import com.promcteam.codex.mccore.config.Filter;
import com.promcteam.fabled.parties.FabledParties;
import com.promcteam.fabled.parties.Party;
import com.promcteam.fabled.parties.lang.ErrorNodes;
import com.promcteam.fabled.parties.lang.PartyNodes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Command to accept a party invitation
 */
public class CmdAccept implements IFunction {

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

        FabledParties fabledParties = (FabledParties) plugin;
        Player        player        = (Player) sender;

        // Check the sender's party status
        Party party = fabledParties.getParty(player);
        if (party != null && party.isInvited(player)) {
            party.accept(player);

            // Join message
            party.sendMessages(fabledParties.getMessage(PartyNodes.PLAYER_JOINED,
                    true,
                    Filter.PLAYER.setReplacement(player.getName())));
        } else {
            fabledParties.sendMessage(player, ErrorNodes.NO_INVITES);
        }
    }
}
