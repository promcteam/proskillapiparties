package com.sucy.party;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.sucy.party.testutil.MockedTest;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PartyTest extends MockedTest {

    private static final Logger log = LoggerFactory.getLogger(PartyTest.class);

    private PlayerMock travja, goflish;

    @BeforeEach
    public void setup() {
        travja = genPlayer("Travja");
        goflish = genPlayer("goflish");
        party = new Party(plugin, travja);
        party.addMember(goflish);

        plugin.addParty(party);
    }

    @AfterEach
    public void tearDown() {
        plugin.removeParty(party);
        server.setPlayers(0);
        players.clear();
    }

    @Test
    public void testPartyLeaderLeavingNewLeaderAssigned() {
        server.executePlayer("pt", "leave");
        travja.assertSaid(ChatColor.translateAlternateColorCodes('&', "&6" + travja.getName() + " &2has left the party&r"));
        assertNotEquals("Travja", party.getLeader().getName());
        log.info("Party ownership is properly reassigned");
    }

    @Test
    public void removeOnQuit() {
        plugin.getConfig().set("remove-on-dc", true);
        plugin.saveConfig();
        plugin.loadConfiguration();

        travja.disconnect();
        server.getPluginManager().assertEventFired(PlayerQuitEvent.class, e -> {
            log.info("Event was fired!");
            return true;
        });

        assertEquals(1, party.getPartySize());
        assertNotEquals("Travja", party.getLeader().getName());
        log.info("Party adjusts properly on disconnect");
    }

    @Test
    public void getLeader() {
        assertEquals("Travja", party.getLeader().getName());
    }
}