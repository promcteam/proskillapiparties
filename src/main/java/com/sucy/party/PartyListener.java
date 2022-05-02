package com.sucy.party;

import com.sucy.party.hook.Hooks;
import com.sucy.party.mccore.PartyBoardManager;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Listener for party mechanics
 */
public class PartyListener implements Listener {

    private final Parties plugin;
    private boolean shared = false;
    private NamespacedKey SHARE_LOCK_METADATA;

    /**
     * Constructor
     *
     * @param plugin plugin reference
     */
    public PartyListener(Parties plugin) {
        this.plugin = plugin;
        SHARE_LOCK_METADATA = new NamespacedKey(plugin, "share_lock");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Handles party chat toggles
     *
     * @param event event details
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (plugin.isToggled(event.getPlayer().getName())) {
            IParty party = Hooks.getParty(event.getPlayer());
            if (party == null || party.isEmpty()) {
                plugin.toggle(event.getPlayer().getName());
                return;
            }
            event.setCancelled(true);
            party.sendMessage(event.getPlayer(), event.getMessage());
        }
    }

    /**
     * Share experience between members
     *
     * @param event event details
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onExpGain(PlayerExperienceGainEvent event) {
        if (event.getSource() == ExpSource.COMMAND) { return; }
        if (plugin.isDebug()) {
            plugin.getLogger().info("Exp already being shared with "+event.getPlayerData().getPlayerName());
        }
        if (shared) { return; }
        IParty party = Hooks.getParty(event.getPlayerData().getPlayer());
        if (plugin.isDebug()) {
            plugin.getLogger().info(event.getPlayerData().getPlayerName()+" has a party? "+(party != null));
        }
        if (party != null) {
            event.setCancelled(true);
            shared = true;
            party.giveExp(event.getPlayerData().getPlayer(), event.getExp(), event.getSource());
            shared = false;
            if (plugin.isDebug()) { plugin.getLogger().info("Exp was shared!"); }
        }
    }

    /**
     * Sets up scoreboards for players when they join
     *
     * @param event event details
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Party party = plugin.getParty(event.getPlayer());
        if (party != null && !party.isEmpty()) {
            PartyBoardManager.applyBoard(plugin, event.getPlayer());
        }
    }

    /**
     * Removes members, changes leaders, or disbands parties upon disconnect
     *
     * @param event event details
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Party party = plugin.getParty(player);
        if (party == null) { return; }

        // Decline invitations on quit
        if (party.isInvited(player)) { party.decline(player); }

        // Removing players on disconnect
        else if (plugin.isRemoveOnDc()) { party.removeMember(player); }

        // Changing leader on disconnect
        else if (plugin.isNewLeaderOnDc() && party.isLeader(player)) { party.changeLeader(); }

        // Removes a party when it's online size reaches 0
        if (party.getOnlinePartySize() == 0) { plugin.removeParty(party); }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();
        shareLockItem(itemStack);
        item.setItemStack(itemStack);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDrop(BlockDropItemEvent event) {
        for (Item item : event.getItems()) {
            ItemStack itemStack = item.getItemStack();
            shareLockItem(itemStack);
            item.setItemStack(itemStack);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        ItemStack itemStack = event.getItem();
        shareLockItem(itemStack);
        event.setItem(itemStack);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) { for (ItemStack drop : event.getDrops()) { shareLockItem(drop); } }

    private void shareLockItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(SHARE_LOCK_METADATA, PersistentDataType.BYTE, (byte) 1);
        itemStack.setItemMeta(itemMeta);
    }

    private void shareUnlockItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().remove(SHARE_LOCK_METADATA);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Handles item distribution to a party
     *
     * @param event event details
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPickup(EntityPickupItemEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) { return; }
        Player player = (Player) entity;
        IParty party = Hooks.getParty((Player) entity);
        if (party == null) { return; }

        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();
        PersistentDataContainer nbt = itemStack.getItemMeta().getPersistentDataContainer();
        boolean sharable = !nbt.has(SHARE_LOCK_METADATA, PersistentDataType.BYTE) || nbt.get(SHARE_LOCK_METADATA, PersistentDataType.BYTE).byteValue() == 0;
        if (sharable) {String mode = plugin.getShareMode().toLowerCase();
            Location location = player.getLocation();
            double radius = plugin.getItemShareRadius();
            switch (mode) {
                case "sequential": {
                    int count = itemStack.getAmount();
                    itemStack.setAmount(1);
                    for (int i = 0; i < count; i++) {
                        Player receiver = party.getSequentialPlayer(location, radius);
                        if (receiver == null) { receiver = player; }
                        receiver.getInventory().addItem(itemStack);
                    }
                    break;
                }
                case "random": {
                    int count = itemStack.getAmount();
                    itemStack.setAmount(1);
                    for (int i = 0; i < count; i++) {
                        Player receiver = party.getRandomPlayer(location, radius);
                        if (receiver == null) { receiver = player; }
                        receiver.getInventory().addItem(itemStack);
                    }
                    break;
                }
                case "sequential-stack": {
                    Player receiver = party.getSequentialPlayer(location, radius);
                    if (receiver == null) { receiver = player; }
                    receiver.getInventory().addItem(itemStack);
                    break;
                }
                case "random-stack": {
                    Player receiver = party.getRandomPlayer(location, radius);
                    if (receiver == null) { receiver = player; }
                    receiver.getInventory().addItem(itemStack);
                    break;
                }
                default: return;
            }
            event.setCancelled(true);
            item.remove();
        } else {
            shareUnlockItem(itemStack);
            item.setItemStack(itemStack);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onFriendlyFire(EntityDamageByEntityEvent event) {
        if (plugin.isFriendlyFireEnabled()) { return; }
        Entity damaged = event.getEntity();
        if (!(damaged instanceof Player)) { return; }
        IParty damagedParty = Hooks.getParty((Player) damaged);
        if (damagedParty == null) { return; }

        Player damager = getUnderlyingPlayer(event.getDamager());
        if (damager == null) { return; }
        IParty damagerParty = Hooks.getParty(damager);

        if (damagedParty == damagerParty) {
            event.setCancelled(true);
        }
    }

    private Player getUnderlyingPlayer(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        } else if (entity instanceof Tameable) {
            AnimalTamer tamer = ((Tameable) entity).getOwner();
            if (tamer instanceof Entity) { return getUnderlyingPlayer((Entity) tamer); }
        } else if (entity instanceof Projectile) {
            ProjectileSource source = ((Projectile) entity).getShooter();
            if (source instanceof Entity) { return getUnderlyingPlayer((Entity) source); }
        } else if (entity instanceof TNTPrimed) {
            Entity source = ((TNTPrimed) entity).getSource();
            if (source != null) { return getUnderlyingPlayer(source); }
        }
        return null;
    }
}
