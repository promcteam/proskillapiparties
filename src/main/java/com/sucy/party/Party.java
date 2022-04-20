package com.sucy.party;

import com.sucy.party.event.PartyExpEvent;
import com.sucy.party.event.PlayerJoinPartyEvent;
import com.sucy.party.event.PlayerLeavePartyEvent;
import com.sucy.party.inject.Server;
import com.sucy.party.lang.IndividualNodes;
import com.sucy.party.lang.PartyNodes;
import com.sucy.party.mccore.PartyBoardManager;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;
import mc.promcteam.engine.mccore.config.Filter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Data for a party
 */
public class Party implements IParty {
    private final ArrayList<String> members = new ArrayList<>();
    private final HashMap<String,Long> invitations = new HashMap<>();
    private final Parties plugin;
    private Player partyLeader;
    private int nextId = -1;

    /**
     * Constructor
     *
     * @param plugin plugin reference
     * @param leader leader of the party
     */
    public Party(Parties plugin, Player leader) {
        this.plugin = plugin;
        this.partyLeader = leader;
        members.add(partyLeader.getName());
    }

    /**
     * @return true if full, false otherwise
     */
    public boolean isFull() {
        checkInvitations();
        return invitations.size()+members.size() >= plugin.getMaxSize();
    }

    /**
     * @return true if the party is empty, false otherwise
     */
    public boolean isEmpty() {
        checkInvitations();
        return invitations.size()+members.size() <= 1;
    }

    /**
     * Retrieves the leader of the party
     *
     * @return party leader
     */
    public Player getLeader() {
        return partyLeader;
    }

    /**
     * Gets the next member in a sequential order
     *
     * @return next player sequentially
     */
    public Player getSequentialPlayer() {
        Player member;
        do {
            nextId = (nextId+1)%members.size();
        }
        while ((member = Server.getPlayer(members.get(nextId))) == null);
        return member;
    }

    /**
     * Gets the next member that is within a sphere, in a sequential order
     *
     * @param location center of the sphere
     * @param radius radius of the sphere
     * @return the next player within the sphere, or null if none was found
     */
    @Override
    @Nullable
    public Player getSequentialPlayer(Location location, double radius) {
        radius *= radius;
        int size = members.size();
        for (int i = 0; i < size; i++) {
            nextId = (nextId+1)%size;
            Player player = Server.getPlayer(members.get(nextId));
            if (player == null) { continue; }
            if (radius >= 0 && (!player.getWorld().equals(location.getWorld()) || player.getLocation().distanceSquared(location) > radius)) { continue; }
            return player;
        }
        return null;
    }

    /**
     * Gets a random player in the party
     *
     * @return random player in the party
     */
    public Player getRandomPlayer() {
        Player member;
        do {
            member = Server.getPlayer(members.get(Parties.RNG.nextInt(members.size())));
        } while (member == null);
        return member;
    }

    /**
     * Gets a random member that is within a sphere, in a sequential order
     *
     * @param location center of the sphere
     * @param radius radius of the sphere
     * @return random member within the sphere, or null if none was found
     */
    @Override
    @Nullable
    public Player getRandomPlayer(Location location, double radius) {
        radius *= radius;
        List<String> members = new ArrayList<>(this.members);
        int size = this.members.size();
        for (int i = 0; i < size; i++) {
            Player player = Server.getPlayer(members.remove(Parties.RNG.nextInt(members.size())));
            if (player == null) { continue; }
            if (radius >= 0 && (!player.getWorld().equals(location.getWorld()) || player.getLocation().distanceSquared(location) > radius)) { continue; }
            return player;
        }
        return null;
    }

    /**
     * Clears all expired invitations from the map
     */
    public void checkInvitations() {
        String[] members = invitations.keySet().toArray(new String[invitations.size()]);
        for (String member : members) {
            if (invitations.get(member) < System.currentTimeMillis()) {
                invitations.remove(member);
                Player player = Server.getPlayer(member);
                if (player != null) {
                    sendMessages(plugin.getMessage(PartyNodes.NO_RESPONSE, true, Filter.PLAYER.setReplacement(player.getName())));
                    plugin.sendMessage(player, IndividualNodes.NO_RESPONSE);
                }
            }
        }
    }

    /**
     * @return size of the party
     */
    public int getPartySize() {
        return members.size();
    }

    /**
     * @return list of names of the members in the party
     */
    public ArrayList<String> getMembers() {
        return members;
    }

    /**
     * @return number of online members in the party
     */
    public int getOnlinePartySize() {
        int counter = 0;
        for (String member : members) {
            if (Server.isOnline(member)) { counter++; }
        }
        return counter;
    }

    /**
     * Checks if the player is on the team
     *
     * @param player player to check
     * @return true if on the team, false otherwise
     */
    public boolean isMember(Player player) {
        return members.contains(player.getName());
    }

    /**
     * Checks if the player has been invited to join the party
     *
     * @param player player to check
     * @return true if invited to join the team, false otherwise
     */
    public boolean isInvited(Player player) {
        checkInvitations();
        return invitations.containsKey(player.getName());
    }

    /**
     * Checks if the player is the leader of the party
     *
     * @param player player to check
     * @return true if they're the leader, false otherwise
     */
    public boolean isLeader(Player player) {
        return partyLeader.equals(player);
    }

    /**
     * Adds a member to the party
     *
     * @param player player to add
     */
    public void invite(Player player) {
        if (!members.contains(player.getName()) && !invitations.containsKey(player.getName())) {
            invitations.put(player.getName(), System.currentTimeMillis()+plugin.getInviteTimeout());
        }
    }

    /**
     * Accepts a player into the party
     *
     * @param player player to accept
     */
    public void accept(Player player) {
        if (invitations.containsKey(player.getName())) {
            invitations.remove(player.getName());

            PlayerJoinPartyEvent event = new PlayerJoinPartyEvent(this, player);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) { return; }

            members.add(player.getName());
            if (members.size() == 2) { PartyBoardManager.applyBoard(plugin, getLeader()); }
            PartyBoardManager.applyBoard(plugin, player);
        }
    }

    /**
     * @param player player to decline
     */
    public void decline(Player player) {
        invitations.remove(player.getName());
    }

    /**
     * Removes a member from the party
     *
     * @param player player to remove
     */
    public void removeMember(Player player) {
        members.remove(player.getName());
        if (isLeader(player) && members.size() > 0) {
            changeLeader();
        }
        PartyBoardManager.clearBoard(plugin, player);
        Bukkit.getPluginManager().callEvent(new PlayerLeavePartyEvent(this, player));
    }

    /**
     * Changes the leader of the party
     */
    public void changeLeader() {
        for (String member : members) {
            if (Server.isOnline(member)) {
                partyLeader = Server.getPlayer(member);
                sendMessages(plugin.getMessage(PartyNodes.NEW_LEADER, true, Filter.PLAYER.setReplacement(partyLeader.getName())));
            }
        }
    }

    /**
     * Removes scoreboards for the party
     */
    public void removeBoards() {
        for (String member : members) {
            Player player = Server.getPlayer(member);
            if (player != null) {
                PartyBoardManager.clearBoard(plugin, player);
            }
        }
    }

    /**
     * Shares experience within the party
     *
     * @param source    player who caused the experience gain
     * @param amount    amount received
     * @param expSource the source type of the gained experience
     */
    public void giveExp(Player source, double amount, ExpSource expSource) {
        if (getOnlinePartySize() == 0) { return; }
        double radiusSq = plugin.getExpShareRadiusSquared();

        // Member modifier
        double baseAmount = amount/(1+(getOnlinePartySize()-1)*plugin.getMemberModifier());
        int level = Server.getLevel(source.getName());

        // Grant exp to all members
        for (String member : members) {
            Player player = Server.getPlayer(member);
            if (player == null) { continue; }
            if (radiusSq >= 0 && (!player.getWorld().equals(source.getWorld()) || player.getLocation().distanceSquared(source.getLocation()) > radiusSq)) { continue; }
            PlayerData info = Server.getPlayerData(player);
            PlayerClass main = info.getMainClass();
            int lvl = main == null ? 0 : main.getLevel();
            int exp = (int) Math.ceil(baseAmount);

            // Level modifier
            if (plugin.getLevelModifier() > 0) {
                int dl = lvl-level;
                exp = (int) Math.ceil(baseAmount*Math.pow(2, -plugin.getLevelModifier()*dl*dl));
            }

            info.giveExp(exp, expSource);
        }

        // Call event
        Bukkit.getPluginManager().callEvent(new PartyExpEvent(source, amount, expSource));
    }

    /**
     * Sends a message to the party
     *
     * @param message message to send
     */
    public void sendMessage(String message) {
        for (String member : members) {
            if (Server.isOnline(member)) {
                Server.getPlayer(member).sendMessage(message);
            }
        }
    }

    /**
     * Sends a list of messages to the party
     *
     * @param messages messages to send
     */
    public void sendMessages(List<String> messages) {
        for (String member : members) {
            if (Server.isOnline(member)) {
                Server.getPlayer(member).sendMessage(messages.toArray(new String[messages.size()]));
            }
        }
    }

    /**
     * Sends a message to all members in the party
     *
     * @param sender  the player who sent a party message
     * @param message message the player typed
     */
    public void sendMessage(Player sender, String message) {
        sendMessages(plugin.getMessage(PartyNodes.CHAT_MESSAGE, true, Filter.PLAYER.setReplacement(sender.getName()), Filter.MESSAGE.setReplacement(message)));
    }

    /**
     * Clears the party scoreboard for the player
     *
     * @param player player to clear for
     */
    public void clearBoard(Player player) {
        PartyBoardManager.clearBoard(plugin, player);
        if (isEmpty()) {
            removeBoards();
        }
    }

    /**
     * Updates all of the scoreboards for the party
     */
    public void updateBoards() {
        removeBoards();
        for (String member : members) {
            PartyBoardManager.applyBoard(plugin, Server.getPlayer(member));
        }
    }
}
