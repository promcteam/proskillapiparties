package com.sucy.party.command;

import com.sucy.party.Parties;
import mc.promcteam.engine.mccore.commands.ConfigurableCommand;
import mc.promcteam.engine.mccore.commands.IFunction;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Command to reload the plugin's config.yml and language.yml
 */
public class CmdReload implements IFunction {
    private final static String MESSAGE = "The config.yml and language.yml have been reloaded";

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
        parties.loadConfiguration();

        parties.getLogger().info(MESSAGE);
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.GREEN + "[ProSkillAPIParties] " + MESSAGE);
        }
    }
}
