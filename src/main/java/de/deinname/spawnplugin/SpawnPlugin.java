package de.deinname.spawnplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    // ==================== ANNOUNCE-BEFEHL ====================
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Nur für /announce
        if (!command.getName().equalsIgnoreCase("announce")) {
            return false;
        }

        // Rechte-Check: nur OPs
        if (!sender.isOp()) {
            sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
            return true;
        }

        // Kein Text angegeben?
        if (args.length == 0) {
            sender.sendMessage("§cBenutzung: /announce <Nachricht>");
            return true;
        }

        // Nachricht zusammensetzen (mit Leerzeichen)
        String message = String.join(" ", args);

        // Schöne Nachricht bauen
        String broadcast =
                "\n" +
                "§8§m                                                       \n" +
                "§6§l📢 ANNOUNCEMENT\n" +
                "§r§f" + message + "\n" +
                "§8§m                                                       \n";

        // An alle Spieler senden
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(broadcast);
        }

        // Bestätigung an den Absender
        sender.sendMessage("§aAnnouncement gesendet: §f" + message);

        // Und ab in die Server-Konsole (für Logs)
        getLogger().info("[ANNOUNCE] " + message);

        return true;
    }
}
