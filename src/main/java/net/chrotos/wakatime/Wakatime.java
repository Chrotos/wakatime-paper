package net.chrotos.wakatime;

import com.google.common.collect.Maps;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Plugin(
        name = "Wakatime",
        version = Metadata.VERSION
)
@ApiVersion(ApiVersion.Target.v1_20)
public final class Wakatime extends JavaPlugin {
    private ApiClient apiClient;
    private final Map<String, Heartbeat> heartbeatQueue = Collections.synchronizedMap(Maps.newHashMap());

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        apiClient = new ApiClient(getConfig().getString("baseurl"), getConfig().getString("apikey"));
        startHeartbeatTask();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        pushHeartbeats();
    }

    public void addHeartbeat(@NonNull Heartbeat heartbeat, @NonNull String user) {
        heartbeatQueue.put(UserMapping.mapUser(user), heartbeat);
    }

    private void startHeartbeatTask() {
        getServer().getScheduler().runTaskTimerAsynchronously(this, this::pushHeartbeats, 30, 20 * 30);
    }

    private void pushHeartbeats() {
        Iterator<Map.Entry<String, Heartbeat>> it = heartbeatQueue.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Heartbeat> entry = it.next();
            apiClient.sendHeartbeat(entry.getValue(), entry.getKey());
            it.remove();
        }
    }
}
