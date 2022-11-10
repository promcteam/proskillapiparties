//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sucy.party.testutil;

import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.PluginClassLoader;
import org.bukkit.plugin.java.PluginClassLoaderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.logging.Level;

public class MockBukkit {
    private static @Nullable CoreServer        mock              = null;
    @Getter
    @Setter
    private static           PluginClassLoader pluginClassLoader = null;

    private MockBukkit() {
    }

    protected static void setServerInstanceToNull() {
        try {
            Field server = Bukkit.class.getDeclaredField("server");
            server.setAccessible(true);
            server.set(null, null);
            mock = null;
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException var1) {
            throw new RuntimeException(var1);
        }
    }

    public static @NotNull CoreServer mock() {
        return mock(new CoreServer(new ServerMock()));
    }

    public static <T extends CoreServer> @NotNull T mock(@NotNull T serverMockImplementation) {
        if (mock != null) {
            throw new IllegalStateException("Already mocking");
        } else {
            mock = serverMockImplementation;
            Level defaultLevel = mock.getLogger().getLevel();
            mock.getLogger().setLevel(Level.WARNING);
            Bukkit.setServer(mock);
            mock.getLogger().setLevel(defaultLevel);
            return serverMockImplementation;
        }
    }

    public static @Nullable CoreServer getOrCreateMock() {
        if (!isMocked()) {
            mock();
        }

        return mock;
    }

    public static @Nullable CoreServer getMock() {
        return mock;
    }

    public static boolean isMocked() {
        return mock != null;
    }

    public static void loadJar(@NotNull String path) {
        try {
            loadJar(new File(path));
        } catch (InvalidPluginException var2) {
            throw new RuntimeException(var2);
        } catch (InvalidDescriptionException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadJar(@NotNull File jarFile) throws InvalidPluginException, InvalidDescriptionException, IOException {
        JavaPluginLoader      loader    = new JavaPluginLoader(mock);
        PluginDescriptionFile desc      = loader.getPluginDescription(jarFile);
        File                  pluginDir = new File(desc.getName());
        pluginDir.mkdir();
        PluginClassLoader classLoad;
        if (pluginClassLoader != null) {
            classLoad = PluginClassLoaderUtils.createPluginClassLoader(loader, null, desc, pluginDir, jarFile, pluginClassLoader);
        } else {
            classLoad = PluginClassLoaderUtils.createPluginClassLoader(loader, null, desc, pluginDir, jarFile, loader.getClass().getClassLoader());
            pluginClassLoader = classLoad;
        }
        Plugin plugin = classLoad.getPlugin();
        mock.getPluginManagerMock().registerLoadedPlugin(plugin);
        mock.getPluginManagerMock().enablePlugin(plugin);
    }

    public static <T extends JavaPlugin> @NotNull T load(@NotNull Class<T> plugin) {
        return load(plugin, new Object[0]);
    }

    public static <T extends JavaPlugin> @NotNull T load(@NotNull Class<T> plugin, @NotNull Object... parameters) {
        ensureMocking();
        JavaPlugin instance = mock.getPluginManagerMock().loadPlugin(plugin, parameters);
        mock.getPluginManagerMock().enablePlugin(instance);
        return plugin.cast(instance);
    }

    public static <T extends JavaPlugin> @NotNull T loadWith(@NotNull Class<T> plugin, @NotNull PluginDescriptionFile descriptionFile, @NotNull Object... parameters) {
        ensureMocking();
        JavaPlugin instance = mock.getPluginManagerMock().loadPlugin(plugin, descriptionFile, parameters);
        mock.getPluginManagerMock().enablePlugin(instance);
        return plugin.cast(instance);
    }

    public static <T extends JavaPlugin> @NotNull T loadWith(@NotNull Class<T> plugin, @NotNull InputStream descriptionInput, Object... parameters) {
        try {
            return loadWith(plugin, new PluginDescriptionFile(descriptionInput), parameters);
        } catch (InvalidDescriptionException var4) {
            throw new RuntimeException(var4);
        }
    }

    public static <T extends JavaPlugin> @NotNull T loadWith(@NotNull Class<T> plugin, @NotNull File descriptionFile, Object... parameters) {
        try {
            return loadWith(plugin, new FileInputStream(descriptionFile), parameters);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    public static <T extends JavaPlugin> @NotNull T loadWith(@NotNull Class<T> plugin, String descriptionFileName, Object... parameters) {
        return loadWith(plugin, ClassLoader.getSystemResourceAsStream(descriptionFileName), parameters);
    }

    public static <T extends JavaPlugin> @NotNull T loadSimple(@NotNull Class<T> plugin, @NotNull Object... parameters) {
        ensureMocking();
        PluginDescriptionFile description = new PluginDescriptionFile(plugin.getSimpleName(), "1.0.0", plugin.getCanonicalName());
        JavaPlugin            instance    = mock.getPluginManagerMock().loadPlugin(plugin, description, parameters);
        mock.getPluginManagerMock().enablePlugin(instance);
        return plugin.cast(instance);
    }

    public static void unmock() {
        if (mock != null) {
            if (mock.getPluginManagerMock() != null) {
                mock.getPluginManagerMock().disablePlugins();
            }

            try {
                mock.getScheduler().shutdown();
            } finally {
                mock.getPluginManagerMock().unload();
                setServerInstanceToNull();
            }

        }
    }

    public static @NotNull MockPlugin createMockPlugin() {
        return createMockPlugin("MockPlugin");
    }

    public static @NotNull MockPlugin createMockPlugin(@NotNull String pluginName) {
        ensureMocking();
        PluginDescriptionFile description = new PluginDescriptionFile(pluginName, "1.0.0", MockPlugin.class.getName());
        JavaPlugin            instance    = mock.getPluginManagerMock().loadPlugin(MockPlugin.class, description);
        mock.getPluginManagerMock().enablePlugin(instance);
        return (MockPlugin) instance;
    }

    public static void ensureMocking() {
        if (!isMocked()) {
            throw new IllegalStateException("Not mocking");
        }
    }
}
