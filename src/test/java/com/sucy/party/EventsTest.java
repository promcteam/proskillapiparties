package com.sucy.party;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.sucy.party.inject.Server;
import com.sucy.party.testutil.MockedTest;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import com.sucy.skill.api.player.PlayerData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

public class EventsTest extends MockedTest {

    private static final Logger log = LoggerFactory.getLogger(EventsTest.class);

    private PlayerMock partyLeader, partyMember;

    @BeforeEach
    public void setup() {
        partyLeader = genPlayer("Travja");
        partyMember = genPlayer("goflish");
        party = spy(new Party(plugin, partyLeader));
        party.addMember(partyMember);

        plugin.addParty(party);
        reset(party);
    }

    @AfterEach
    public void tearDown() {
        plugin.removeParty(party);
        server.setPlayers(0);
    }

    @Test
    public void experienceGainEventIsShared() {
       new PlayerExperienceGainEvent(
                Server.getClass(partyLeader), 80, ExpSource.EXP_BOTTLE
        ).callEvent();
        PlayerData pd = activePlayerData.get(partyLeader.getUniqueId());
        PlayerData pd2 = activePlayerData.get(partyMember.getUniqueId());

        verify(party, times(1))
                .giveExp(partyLeader, 80, ExpSource.EXP_BOTTLE);
        verify(pd, times(1)).giveExp(anyDouble(), any(ExpSource.class));
        verify(pd2, times(1)).giveExp(anyDouble(), any(ExpSource.class));
    }

}
