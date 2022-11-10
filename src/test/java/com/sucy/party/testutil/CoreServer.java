package com.sucy.party.testutil;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;
import be.seeseemelk.mockbukkit.scheduler.BukkitSchedulerMock;
import com.destroystokyo.paper.entity.ai.MobGoals;
import io.papermc.paper.datapack.DatapackManager;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class CoreServer implements Server {

    private       SimplePluginManager pm;
    @Getter
    private ServerMock          mock;

    public CoreServer(ServerMock mock) {
        super();
        this.mock = mock;
        pm = new SimplePluginManager(this, (SimpleCommandMap) getCommandMap());
    }

    @Override
    public @NotNull File getPluginsFolder() {
        return mock.getPluginsFolder();
    }

    @Override
    public @NotNull String getName() {
        return mock.getName();
    }

    @Override
    public @NotNull String getVersion() {
        return mock.getVersion();
    }

    @Override
    public @NotNull String getBukkitVersion() {
        return mock.getBukkitVersion();
    }

    @Override
    public @NotNull String getMinecraftVersion() {
        return mock.getMinecraftVersion();
    }

    @Override
    public @NotNull Collection<? extends Player> getOnlinePlayers() {
        return mock.getOnlinePlayers();
    }

    @Override
    public int getMaxPlayers() {
        return mock.getMaxPlayers();
    }

    @Override
    public void setMaxPlayers(int i) {
        mock.setMaxPlayers(i);
    }

    @Override
    public int getPort() {
        return mock.getPort();
    }

    @Override
    public int getViewDistance() {
        return mock.getViewDistance();
    }

    @Override
    public int getSimulationDistance() {
        return mock.getSimulationDistance();
    }

    @Override
    public @NotNull String getIp() {
        return mock.getIp();
    }

    @Override
    public @NotNull String getWorldType() {
        return mock.getWorldType();
    }

    @Override
    public boolean getGenerateStructures() {
        return mock.getGenerateStructures();
    }

    @Override
    public int getMaxWorldSize() {
        return mock.getMaxWorldSize();
    }

    @Override
    public boolean getAllowEnd() {
        return mock.getAllowEnd();
    }

    @Override
    public boolean getAllowNether() {
        return mock.getAllowNether();
    }

    @Override
    public @NotNull String getResourcePack() {
        return mock.getResourcePack();
    }

    @Override
    public @NotNull String getResourcePackHash() {
        return mock.getResourcePackHash();
    }

    @Override
    public @NotNull String getResourcePackPrompt() {
        return mock.getResourcePackPrompt();
    }

    @Override
    public boolean isResourcePackRequired() {
        return mock.isResourcePackRequired();
    }

    @Override
    public boolean hasWhitelist() {
        return mock.hasWhitelist();
    }

    @Override
    public void setWhitelist(boolean b) {
        mock.setWhitelist(b);
    }

    @Override
    public boolean isWhitelistEnforced() {
        return mock.isWhitelistEnforced();
    }

    @Override
    public void setWhitelistEnforced(boolean b) {
        mock.setWhitelistEnforced(b);
    }

    @Override
    public @NotNull Set<OfflinePlayer> getWhitelistedPlayers() {
        return mock.getWhitelistedPlayers();
    }

    @Override
    public void reloadWhitelist() {
        mock.reloadWhitelist();
    }

    @Override
    public int broadcastMessage(@NotNull String s) {
        return mock.broadcastMessage(s);
    }

    @Override
    public @NotNull String getUpdateFolder() {
        return mock.getUpdateFolder();
    }

    @Override
    public @NotNull File getUpdateFolderFile() {
        return mock.getUpdateFolderFile();
    }

    @Override
    public long getConnectionThrottle() {
        return mock.getConnectionThrottle();
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return mock.getTicksPerAnimalSpawns();
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return mock.getTicksPerMonsterSpawns();
    }

    @Override
    public int getTicksPerWaterSpawns() {
        return mock.getTicksPerWaterSpawns();
    }

    @Override
    public int getTicksPerWaterAmbientSpawns() {
        return mock.getTicksPerWaterAmbientSpawns();
    }

    @Override
    public int getTicksPerWaterUndergroundCreatureSpawns() {
        return mock.getTicksPerWaterUndergroundCreatureSpawns();
    }

    @Override
    public int getTicksPerAmbientSpawns() {
        return mock.getTicksPerAmbientSpawns();
    }

    @Override
    public int getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
        return mock.getTicksPerSpawns(spawnCategory);
    }

    @Override
    public @Nullable Player getPlayer(@NotNull String s) {
        return mock.getPlayer(s);
    }

    @Override
    public @Nullable Player getPlayerExact(@NotNull String s) {
        return mock.getPlayerExact(s);
    }

    @Override
    public @NotNull List<Player> matchPlayer(@NotNull String s) {
        return mock.matchPlayer(s);
    }

    @Override
    public @Nullable Player getPlayer(@NotNull UUID uuid) {
        return mock.getPlayer(uuid);
    }

    @Override
    public @Nullable UUID getPlayerUniqueId(@NotNull String s) {
        return mock.getPlayerUniqueId(s);
    }

    public PluginManagerMock getPluginManagerMock() {
        return mock.getPluginManager();
    }

    public PluginManager getPluginManager() {
        return pm;
    }

    @Override
    public @NotNull BukkitSchedulerMock getScheduler() {
        return mock.getScheduler();
    }

    @Override
    public @NotNull ServicesManager getServicesManager() {
        return mock.getServicesManager();
    }

    @Override
    public @NotNull List<World> getWorlds() {
        return mock.getWorlds();
    }

    @Override
    public boolean isTickingWorlds() {
        return mock.isTickingWorlds();
    }

    @Override
    public @Nullable World createWorld(@NotNull WorldCreator worldCreator) {
        return mock.createWorld(worldCreator);
    }

    @Override
    public boolean unloadWorld(@NotNull String s, boolean b) {
        return mock.unloadWorld(s, b);
    }

    @Override
    public boolean unloadWorld(@NotNull World world, boolean b) {
        return mock.unloadWorld(world, b);
    }

    @Override
    public @Nullable World getWorld(@NotNull String s) {
        return mock.getWorld(s);
    }

    @Override
    public @Nullable World getWorld(@NotNull UUID uuid) {
        return mock.getWorld(uuid);
    }

    @Override
    public @Nullable World getWorld(@NotNull NamespacedKey namespacedKey) {
        return mock.getWorld(namespacedKey);
    }

    @Override
    public @NotNull WorldBorder createWorldBorder() {
        return mock.createWorldBorder();
    }

    @Override
    public @Nullable MapView getMap(int i) {
        return mock.getMap(i);
    }

    @Override
    public @NotNull MapView createMap(@NotNull World world) {
        return mock.createMap(world);
    }

    @Override
    public @NotNull ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType) {
        return mock.createExplorerMap(world, location, structureType);
    }

    @Override
    public @NotNull ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType, int i, boolean b) {
        return mock.createExplorerMap(world, location, structureType, i, b);
    }

    @Override
    public void reload() {
        mock.reload();
    }

    @Override
    public void reloadData() {
        mock.reloadData();
    }

    @Override
    public @NotNull Logger getLogger() {
        return mock.getLogger();
    }

    @Override
    public @Nullable PluginCommand getPluginCommand(@NotNull String s) {
        return mock.getPluginCommand(s);
    }

    @Override
    public void savePlayers() {
        mock.savePlayers();
    }

    @Override
    public boolean dispatchCommand(@NotNull CommandSender commandSender, @NotNull String s) throws CommandException {
        return mock.dispatchCommand(commandSender, s);
    }

    @Override
    public boolean addRecipe(@Nullable Recipe recipe) {
        return mock.addRecipe(recipe);
    }

    @Override
    public @NotNull List<Recipe> getRecipesFor(@NotNull ItemStack itemStack) {
        return mock.getRecipesFor(itemStack);
    }

    @Override
    public @Nullable Recipe getRecipe(@NotNull NamespacedKey namespacedKey) {
        return mock.getRecipe(namespacedKey);
    }

    @Override
    public @Nullable Recipe getCraftingRecipe(@NotNull ItemStack[] itemStacks, @NotNull World world) {
        return mock.getCraftingRecipe(itemStacks, world);
    }

    @Override
    public @NotNull ItemStack craftItem(@NotNull ItemStack[] itemStacks, @NotNull World world, @NotNull Player player) {
        return mock.craftItem(itemStacks, world, player);
    }

    @Override
    public @NotNull Iterator<Recipe> recipeIterator() {
        return mock.recipeIterator();
    }

    @Override
    public void clearRecipes() {
        mock.clearRecipes();
    }

    @Override
    public void resetRecipes() {
        mock.resetRecipes();
    }

    @Override
    public boolean removeRecipe(@NotNull NamespacedKey namespacedKey) {
        return mock.removeRecipe(namespacedKey);
    }

    @Override
    public @NotNull Map<String, String[]> getCommandAliases() {
        return mock.getCommandAliases();
    }

    @Override
    public int getSpawnRadius() {
        return mock.getSpawnRadius();
    }

    @Override
    public void setSpawnRadius(int i) {
        mock.setSpawnRadius(i);
    }

    @Override
    public boolean shouldSendChatPreviews() {
        return mock.shouldSendChatPreviews();
    }

    @Override
    public boolean isEnforcingSecureProfiles() {
        return mock.isEnforcingSecureProfiles();
    }

    @Override
    public boolean getHideOnlinePlayers() {
        return mock.getHideOnlinePlayers();
    }

    @Override
    public boolean getOnlineMode() {
        return mock.getOnlineMode();
    }

    @Override
    public boolean getAllowFlight() {
        return mock.getAllowFlight();
    }

    @Override
    public boolean isHardcore() {
        return mock.isHardcore();
    }

    @Override
    public void shutdown() {
        mock.shutdown();
    }

    @Override
    public int broadcast(@NotNull String s, @NotNull String s1) {
        return mock.broadcast(s, s1);
    }

    @Override
    public int broadcast(@NotNull Component component) {
        return mock.broadcast(component);
    }

    @Override
    public int broadcast(@NotNull Component component, @NotNull String s) {
        return mock.broadcast(component, s);
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull String s) {
        return mock.getOfflinePlayer(s);
    }

    @Override
    public @Nullable OfflinePlayer getOfflinePlayerIfCached(@NotNull String s) {
        return mock.getOfflinePlayerIfCached(s);
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID uuid) {
        return mock.getOfflinePlayer(uuid);
    }

    @Override
    public @NotNull PlayerProfile createPlayerProfile(@Nullable UUID uuid, @Nullable String s) {
        return mock.createPlayerProfile(uuid, s);
    }

    @Override
    public @NotNull PlayerProfile createPlayerProfile(@NotNull UUID uuid) {
        return mock.createPlayerProfile(uuid);
    }

    @Override
    public @NotNull PlayerProfile createPlayerProfile(@NotNull String s) {
        return mock.createPlayerProfile(s);
    }

    @Override
    public @NotNull Set<String> getIPBans() {
        return mock.getIPBans();
    }

    @Override
    public void banIP(@NotNull String s) {
        mock.banIP(s);
    }

    @Override
    public void unbanIP(@NotNull String s) {
        mock.unbanIP(s);
    }

    @Override
    public @NotNull Set<OfflinePlayer> getBannedPlayers() {
        return mock.getBannedPlayers();
    }

    @Override
    public @NotNull BanList getBanList(BanList.@NotNull Type type) {
        return mock.getBanList(type);
    }

    @Override
    public @NotNull Set<OfflinePlayer> getOperators() {
        return mock.getOperators();
    }

    @Override
    public @NotNull GameMode getDefaultGameMode() {
        return mock.getDefaultGameMode();
    }

    @Override
    public void setDefaultGameMode(@NotNull GameMode gameMode) {
        mock.setDefaultGameMode(gameMode);
    }

    @Override
    public @NotNull ConsoleCommandSender getConsoleSender() {
        return mock.getConsoleSender();
    }

    @Override
    public @NotNull CommandSender createCommandSender(@NotNull Consumer<? super Component> consumer) {
        return mock.createCommandSender(consumer);
    }

    @Override
    public @NotNull File getWorldContainer() {
        return mock.getWorldContainer();
    }

    @Override
    public @NotNull OfflinePlayer[] getOfflinePlayers() {
        return mock.getOfflinePlayers();
    }

    @Override
    public @NotNull Messenger getMessenger() {
        return mock.getMessenger();
    }

    @Override
    public @NotNull HelpMap getHelpMap() {
        return mock.getHelpMap();
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder inventoryHolder, @NotNull InventoryType inventoryType) {
        return mock.createInventory(inventoryHolder, inventoryType);
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder inventoryHolder, @NotNull InventoryType inventoryType, @NotNull Component component) {
        return mock.createInventory(inventoryHolder, inventoryType, component);
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder inventoryHolder, @NotNull InventoryType inventoryType, @NotNull String s) {
        return mock.createInventory(inventoryHolder, inventoryType, s);
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder inventoryHolder, int i) throws IllegalArgumentException {
        return mock.createInventory(inventoryHolder, i);
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder inventoryHolder, int i, @NotNull Component component) throws IllegalArgumentException {
        return mock.createInventory(inventoryHolder, i, component);
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder inventoryHolder, int i, @NotNull String s) throws IllegalArgumentException {
        return mock.createInventory(inventoryHolder, i, s);
    }

    @Override
    public @NotNull Merchant createMerchant(@Nullable Component component) {
        return mock.createMerchant(component);
    }

    @Override
    public @NotNull Merchant createMerchant(@Nullable String s) {
        return mock.createMerchant(s);
    }

    @Override
    public int getMaxChainedNeighborUpdates() {
        return mock.getMaxChainedNeighborUpdates();
    }

    @Override
    public int getMonsterSpawnLimit() {
        return mock.getMonsterSpawnLimit();
    }

    @Override
    public int getAnimalSpawnLimit() {
        return mock.getAnimalSpawnLimit();
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return mock.getWaterAnimalSpawnLimit();
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        return mock.getWaterAmbientSpawnLimit();
    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
        return mock.getWaterUndergroundCreatureSpawnLimit();
    }

    @Override
    public int getAmbientSpawnLimit() {
        return mock.getAmbientSpawnLimit();
    }

    @Override
    public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
        return mock.getSpawnLimit(spawnCategory);
    }

    @Override
    public boolean isPrimaryThread() {
        return mock.isPrimaryThread();
    }

    @Override
    public @NotNull Component motd() {
        return mock.motd();
    }

    @Override
    public @NotNull String getMotd() {
        return mock.getMotd();
    }

    @Override
    public @Nullable Component shutdownMessage() {
        return mock.shutdownMessage();
    }

    @Override
    public @Nullable String getShutdownMessage() {
        return mock.getShutdownMessage();
    }

    @Override
    public Warning.@NotNull WarningState getWarningState() {
        return mock.getWarningState();
    }

    @Override
    public @NotNull ItemFactory getItemFactory() {
        return mock.getItemFactory();
    }

    @Override
    public @NotNull ScoreboardManager getScoreboardManager() {
        return mock.getScoreboardManager();
    }

    @Override
    public @NotNull Criteria getScoreboardCriteria(@NotNull String s) {
        return mock.getScoreboardCriteria(s);
    }

    @Override
    public @Nullable CachedServerIcon getServerIcon() {
        return mock.getServerIcon();
    }

    @Override
    public @NotNull CachedServerIcon loadServerIcon(@NotNull File file) throws IllegalArgumentException, Exception {
        return mock.loadServerIcon(file);
    }

    @Override
    public @NotNull CachedServerIcon loadServerIcon(@NotNull BufferedImage bufferedImage) throws IllegalArgumentException, Exception {
        return mock.loadServerIcon(bufferedImage);
    }

    @Override
    public void setIdleTimeout(int i) {
        mock.setIdleTimeout(i);
    }

    @Override
    public int getIdleTimeout() {
        return mock.getIdleTimeout();
    }

    @Override
    public ChunkGenerator.@NotNull ChunkData createChunkData(@NotNull World world) {
        return mock.createChunkData(world);
    }

    @Override
    public ChunkGenerator.@NotNull ChunkData createVanillaChunkData(@NotNull World world, int i, int i1) {
        return mock.createVanillaChunkData(world, i, i1);
    }

    @Override
    public @NotNull BossBar createBossBar(@Nullable String s, @NotNull BarColor barColor, @NotNull BarStyle barStyle, @NotNull BarFlag... barFlags) {
        return mock.createBossBar(s, barColor, barStyle, barFlags);
    }

    @Override
    public @NotNull KeyedBossBar createBossBar(@NotNull NamespacedKey namespacedKey, @Nullable String s, @NotNull BarColor barColor, @NotNull BarStyle barStyle, @NotNull BarFlag... barFlags) {
        return mock.createBossBar(namespacedKey, s, barColor, barStyle, barFlags);
    }

    @Override
    public @NotNull Iterator<KeyedBossBar> getBossBars() {
        return mock.getBossBars();
    }

    @Override
    public @Nullable KeyedBossBar getBossBar(@NotNull NamespacedKey namespacedKey) {
        return mock.getBossBar(namespacedKey);
    }

    @Override
    public boolean removeBossBar(@NotNull NamespacedKey namespacedKey) {
        return mock.removeBossBar(namespacedKey);
    }

    @Override
    public @Nullable Entity getEntity(@NotNull UUID uuid) {
        return mock.getEntity(uuid);
    }

    @Override
    public @NotNull double[] getTPS() {
        return mock.getTPS();
    }

    @Override
    public @NotNull long[] getTickTimes() {
        return mock.getTickTimes();
    }

    @Override
    public double getAverageTickTime() {
        return mock.getAverageTickTime();
    }

    @Override
    public @NotNull CommandMap getCommandMap() {
        return mock.getCommandMap();
    }

    @Override
    public @Nullable Advancement getAdvancement(@NotNull NamespacedKey namespacedKey) {
        return mock.getAdvancement(namespacedKey);
    }

    @Override
    public @NotNull Iterator<Advancement> advancementIterator() {
        return mock.advancementIterator();
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull Material material) {
        return mock.createBlockData(material);
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull Material material, @Nullable Consumer<BlockData> consumer) {
        return mock.createBlockData(material, consumer);
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull String s) throws IllegalArgumentException {
        return mock.createBlockData(s);
    }

    @Override
    public @NotNull BlockData createBlockData(@Nullable Material material, @Nullable String s) throws IllegalArgumentException {
        return mock.createBlockData(material, s);
    }

    @Override
    public @Nullable <T extends Keyed> Tag<T> getTag(@NotNull String s, @NotNull NamespacedKey namespacedKey, @NotNull Class<T> aClass) {
        return mock.getTag(s, namespacedKey, aClass);
    }

    @Override
    public @NotNull <T extends Keyed> Iterable<Tag<T>> getTags(@NotNull String s, @NotNull Class<T> aClass) {
        return mock.getTags(s, aClass);
    }

    @Override
    public @Nullable LootTable getLootTable(@NotNull NamespacedKey namespacedKey) {
        return mock.getLootTable(namespacedKey);
    }

    @Override
    public @NotNull List<Entity> selectEntities(@NotNull CommandSender commandSender, @NotNull String s) throws IllegalArgumentException {
        return mock.selectEntities(commandSender, s);
    }

    @Override
    public @NotNull StructureManager getStructureManager() {
        return mock.getStructureManager();
    }

    @Override
    public @Nullable <T extends Keyed> Registry<T> getRegistry(@NotNull Class<T> aClass) {
        return mock.getRegistry(aClass);
    }

    @Override
    public @NotNull UnsafeValues getUnsafe() {
        return mock.getUnsafe();
    }

    @Override
    public @NotNull Spigot spigot() {
        return mock.spigot();
    }

    @Override
    public void reloadPermissions() {
        mock.reloadPermissions();
    }

    @Override
    public boolean reloadCommandAliases() {
        return mock.reloadCommandAliases();
    }

    @Override
    public boolean suggestPlayerNamesWhenNullTabCompletions() {
        return mock.suggestPlayerNamesWhenNullTabCompletions();
    }

    @Override
    public @NotNull String getPermissionMessage() {
        return mock.getPermissionMessage();
    }

    @Override
    public @NotNull Component permissionMessage() {
        return mock.permissionMessage();
    }

    @Override
    public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfile(@NotNull UUID uuid) {
        return mock.createProfile(uuid);
    }

    @Override
    public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfile(@NotNull String s) {
        return mock.createProfile(s);
    }

    @Override
    public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfile(@Nullable UUID uuid, @Nullable String s) {
        return mock.createProfile(uuid, s);
    }

    @Override
    public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfileExact(@Nullable UUID uuid, @Nullable String s) {
        return mock.createProfileExact(uuid, s);
    }

    @Override
    public int getCurrentTick() {
        return mock.getCurrentTick();
    }

    @Override
    public boolean isStopping() {
        return mock.isStopping();
    }

    @Override
    public @NotNull MobGoals getMobGoals() {
        return mock.getMobGoals();
    }

    @Override
    public @NotNull DatapackManager getDatapackManager() {
        return mock.getDatapackManager();
    }

    @Override
    public @NotNull PotionBrewer getPotionBrewer() {
        return mock.getPotionBrewer();
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return mock.audiences();
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin plugin, @NotNull String s, @NotNull byte[] bytes) {
        mock.sendPluginMessage(plugin, s, bytes);
    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        return mock.getListeningPluginChannels();
    }

    public void setPlayers(int i) {
        mock.setPlayers(i);
    }

    public PlayerMock addPlayer(PlayerMock playerMock) {
        mock.addPlayer(playerMock);
        return playerMock;
    }
}
