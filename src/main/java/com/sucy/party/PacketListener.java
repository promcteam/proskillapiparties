package com.sucy.party;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.sucy.party.event.PlayerLeavePartyEvent;
import com.sucy.party.event.PostPlayerJoinPartyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class PacketListener implements Listener{
    private ProtocolManager manager;
    private Parties parties;
    public PacketListener(Parties plugin){
        this.parties = plugin;
        manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new GlowingAdapter(parties));
    }
    private void sendDummyMetadataPacket(Player receiver, Player entity){
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId());

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setObject(0, serializer, (byte) 0);
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            manager.sendServerPacket(receiver, packet);
        }catch (InvocationTargetException ex){}
    }
    private void updatePartyGlow(Player player, Party party){
        for(UUID id : party.getMembers()){
            Player partyPlayer = Bukkit.getPlayer(id);
            if(partyPlayer == null) continue;
            if(!player.getWorld().getName().equals(partyPlayer.getWorld().getName())) return;
            sendDummyMetadataPacket(player, partyPlayer);
            sendDummyMetadataPacket(partyPlayer,player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PostPlayerJoinPartyEvent event){
        updatePartyGlow(event.getPlayer(),event.getParty());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerLeavePartyEvent event){
        updatePartyGlow(event.getPlayer(),event.getParty());
    }
    class GlowingAdapter extends PacketAdapter {
        public GlowingAdapter(Plugin plugin) {
            super(plugin, PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        }

        @Override
        public void onPacketSending(PacketEvent event) {
            Party party = parties.getJoinedParty(event.getPlayer());
            if(party == null)return;
            PacketContainer container = event.getPacket();
            Entity entity = manager.getEntityFromID(event.getPlayer().getWorld(),container.getIntegers().read(0));
            Player player = Bukkit.getPlayer(entity.getUniqueId());
            if(player == null) return;

            if(party.getMembers().contains(player.getUniqueId())){
                try {
                    List<WrappedWatchableObject> metadatas = event.getPacket().getWatchableCollectionModifier().read(0);
                    WrappedDataWatcher.WrappedDataWatcherObject watcherObject = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
                    final WrappedDataWatcher dataWatcher = new WrappedDataWatcher(metadatas);
                    final byte value = (byte) dataWatcher.getObject(watcherObject);
                    dataWatcher.setObject(watcherObject, (byte) (value | 0x40));
                    container.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
                }catch (FieldAccessException ex){}
            }
        }
    }
    public void cleanup(){
        manager.removePacketListeners(parties);
    }


}
