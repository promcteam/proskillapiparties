package com.sucy.party;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.sucy.party.event.PartyExpEvent;
import com.sucy.party.event.PlayerJoinPartyEvent;
import com.sucy.party.event.PlayerLeavePartyEvent;
import com.sucy.party.testutil.MockedTest;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cyberiantiger.minecraft.instances.command.PartyLeader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;

public class PartyTest extends MockedTest {

    private static final Logger log = LoggerFactory.getLogger(PartyTest.class);

    private PlayerMock partyLeader, partyMember;

    @BeforeEach
    public void setup() {
        partyLeader = genPlayer("Travja");
        partyMember = genPlayer("goflish");
        party = new Party(plugin, partyLeader);
        party.addMember(partyMember);

        plugin.addParty(party);
    }

    @AfterEach
    public void tearDown() {
        plugin.removeParty(party);
        server.setPlayers(0);
    }

    @Test
    public void testPartyLeaderLeavingNewLeaderAssigned() {
        partyLeader.performCommand("pt leave");
        partyLeader.assertSaid(ChatColor.translateAlternateColorCodes('&', "&6" + partyLeader.getName() + " &2has left the party&r"));
        assertEquals(1, party.getPartySize());
        assertNotEquals(partyLeader.getName(), party.getLeader().getName());
        log.info("Party ownership is properly reassigned");
    }

    @Test
    public void removeOnQuit() {
        plugin.getConfig().set("remove-on-dc", true);
        plugin.saveConfig();
        plugin.loadConfiguration();

        partyLeader.disconnect();

        assertEventFired(PlayerQuitEvent.class);
        assertEquals(1, party.getPartySize());
        assertNotEquals(partyLeader.getName(), party.getLeader().getName());
        log.info("Party adjusts properly on disconnect");
    }

    @Test
    public void getLeader() {
        assertEquals(partyLeader.getName(), party.getLeader().getName());
    }

    @Test
    public void acceptAddsPlayer() {
        PlayerMock player = genPlayer("Joe");
        party.invite(player);
        party.accept(player);

        assertEventFired(PlayerJoinPartyEvent.class);
        assertEquals(3, party.getPartySize());
    }

    @Test
    public void removeRemovesPlayer() {
        party.removeMember(partyMember);

        assertEventFired(PlayerLeavePartyEvent.class);
        assertEquals(1, party.getPartySize());
    }

    @Test
    public void giveExp() {
        party.giveExp(partyLeader, 100, ExpSource.MOB);
        PlayerData leaderData  = activePlayerData.get(partyLeader.getUniqueId());
        PlayerData memberData = activePlayerData.get(partyMember.getUniqueId());

        assertEventFired(PartyExpEvent.class, e -> {
            verify(leaderData).giveExp(e.getAmount(), e.getExpSource());
            verify(memberData).giveExp(e.getAmount(), e.getExpSource());

            return e.getAmount() != 0;
        });
    }

    @Test
    public void giveExpSoloGetsAll() {
        party.removeMember(partyMember);
        party.giveExp(partyLeader, 100, ExpSource.MOB);
        PlayerData leaderData  = activePlayerData.get(partyLeader.getUniqueId());

        assertEventFired(PartyExpEvent.class, e -> {
            verify(leaderData).giveExp(100, e.getExpSource());

            return e.getAmount() != 0;
        });
    }
}