package de.deinname.spawnplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnPlugin extends JavaPlugin implements Listener {

    private final double X = -396.0;
    private final double Y = 279.0;
    private final double Z = -119.0;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("SpawnPlugin aktiviert!");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskLater(this, () -> {
                World world = player.getWorld();
                player.teleport(new Location(world, X, Y, Z));
                player.sendMessage("§aWillkommen! Du wurdest zum Startpunkt teleportiert.");
            }, 1L);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.isBedSpawn() && !event.isAnchorSpawn()) {
            World world = event.getPlayer().getWorld();
            event.setRespawnLocation(new Location(world, X, Y, Z));
        }
    }
}
