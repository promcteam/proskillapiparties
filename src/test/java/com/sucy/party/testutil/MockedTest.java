package com.sucy.party.testutil;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.sucy.party.Parties;
import com.sucy.party.Party;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class MockedTest {
    protected static ServerMock       server;
    protected static Parties          plugin;
    protected        Party            party;
    protected        List<PlayerMock> players = new ArrayList<>();

    @BeforeAll
    public static void setupServer() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Parties.class);
    }

    @AfterAll
    public static void destroy() {
        MockBukkit.unmock();
    }

    public PlayerMock genPlayer(String name) {
        return genPlayer(name, true);
    }

    public PlayerMock genPlayer(String name, boolean op) {
//        PlayerMock pm = server.addPlayer(name);
        PlayerMock pm = new PlayerMock(server, name, UUID.randomUUID());
        server.addPlayer(pm);
        players.add(pm);
        pm.setOp(op);

        return pm;
    }
}
