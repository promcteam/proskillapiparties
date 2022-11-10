package com.sucy.party.testutil;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.sucy.party.Parties;
import com.sucy.party.Party;
import com.sucy.skill.SkillAPI;
import org.bukkit.event.Event;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class MockedTest {
    private   Logger           log     = LoggerFactory.getLogger(MockedTest.class);
    protected CoreServer       server;
    protected Parties          plugin;
    protected Party            party;
    protected List<PlayerMock> players = new ArrayList<>();

    @BeforeAll
    public void setupServer() throws InvalidPluginException, IOException, InvalidDescriptionException {
        server = MockBukkit.mock();
        MockBukkit.loadJar(DependencyResolver.resolve("com.promcteam:promccore:1.0.3.10-SNAPSHOT"));
        MockBukkit.loadJar(DependencyResolver.resolve("com.promcteam:proskillapi:1.1.7.18-SNAPSHOT"));
        plugin = MockBukkit.load(Parties.class);
    }

    @AfterAll
    public static void destroy() {
        MockBukkit.unmock();
    }

    private static InputStream loadSkillResource(String name) {
        return SkillAPI.class.getClassLoader().getResourceAsStream(name);
    }

    public PlayerMock genPlayer(String name) {
        return genPlayer(name, true);
    }

    public PlayerMock genPlayer(String name, boolean op) {
//        PlayerMock pm = server.addPlayer(name);
        PlayerMock pm = server.addPlayer(new PlayerMock(server.getMock(), name, UUID.randomUUID()));
        players.add(pm);
        pm.setOp(op);

        return pm;
    }

    public <T extends Event> void assertEventFired(Class<T> clazz) {
        server.getPluginManagerMock().assertEventFired(clazz);
    }

    public <T extends Event> void assertEventFired(Class<T> clazz, Predicate<T> predicate) {
        server.getPluginManagerMock().assertEventFired(clazz, predicate);
    }
}
