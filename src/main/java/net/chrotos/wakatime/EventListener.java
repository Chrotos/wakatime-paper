package net.chrotos.wakatime;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerCommandEvent;

@RequiredArgsConstructor
public class EventListener implements Listener {
    private final Wakatime plugin;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        sendHeartbeat(event.getPlayer());
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        sendHeartbeat(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        sendHeartbeat(event.getPlayer());
    }

    private void sendHeartbeat(@NonNull Player player) {
        plugin.addHeartbeat(Heartbeat.builder()
                .entity(player.getWorld().getName())
                .type("app")
                .category("building")
                .time(((float) (System.currentTimeMillis())) / 1000F)
                .project(plugin.getServer().getMotd())
                .build(), player.getName());
    }
}
