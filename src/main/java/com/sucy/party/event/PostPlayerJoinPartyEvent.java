package com.sucy.party.event;

import com.sucy.party.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PostPlayerJoinPartyEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Party party;
    private final Player player;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PostPlayerJoinPartyEvent(Party party, Player player) {
        this.party = party;
        this.player = player;
    }

    /**
     * @return the party the player joined to
     */
    public Party getParty() {
        return party;
    }

    /**
     * @return the player that joined the party
     */
    public Player getPlayer() {
        return player;
    }

}
